package rausku.algorithm;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.Scene;

import java.util.List;

import static java.lang.Float.max;
import static rausku.math.FloatMath.abs;
import static rausku.math.FloatMath.pow;

public class RecursiveRayTracer implements RayTracer {

    private static final int MAX_DEPTH = 100;

    private final Scene scene;

    private boolean debug;

    public RecursiveRayTracer(Scene scene) {
        this.scene = scene;
        debug = false;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    boolean interceptsRay(Ray ray) {
        List<SceneObject> objects = scene.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = scene.getInverseTransforms().get(i);
            Ray transform1 = transform.transform(ray);
            Intercept intercept2 = object.getIntercept(transform1);
            float intercept = intercept2.intercept;
            if (Float.isFinite(intercept) && intercept > 0) {
                if (this.debug) {
                    ray.addDebug(String.format("light ray %s", transform1));
                    ray.addDebug(String.format("intercept %d, %s", i, intercept2));
                }
                return true;
            }
        }
        if (this.debug) {
            ray.addDebug("no intercept");
        }
        return false;
    }

    Color resolveRayColor(int depth, float reflectiveness, Ray ray) {

        if (depth > MAX_DEPTH) {
            if (this.debug) {
                addDebugString(ray, "Max depth reached");
            }
            return scene.getAmbientLight().getColor();
        }

        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept intercept = null;
        int index = -1;

        List<SceneObject> objects = scene.getObjects();
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = scene.getInverseTransforms().get(i);
            Ray transform1 = transform.transform(ray);
            Intercept objectIntercept = object.getIntercept(transform1);
            float interceptValue = objectIntercept.intercept;
            if (interceptValue > SceneObject.INTERCEPT_NEAR && interceptValue < closestIntercept) {
                index = i;
                closestIntercept = interceptValue;
                intercept = objectIntercept;
            }
        }

        if (this.debug) {
            if (index >= 0) {
                addDebugString(ray, "depth=%d object=%d[%s] %s", depth, index, objects.get(index), intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (intercept != null) {
            return getColorFromObject(depth, reflectiveness, intercept, ray, scene.getTransforms().get(index), objects.get(index));
        }

        // Specular reflection of the light source itself when nothing is hit
        for (DirectionalLight light : scene.getLights()) {
            if (light.intercepts(ray)) {
                return light.getColor().mul(10);
            }
        }

        // nothing hit
        return scene.getAmbientLight().getColor();
    }

    @Override
    public Color resolveRayColor(float reflectiveness, Ray ray) {
        return resolveRayColor(0, reflectiveness, ray);
    }

    Color getSpecularReflection(int depth, float reflectiveness, Ray ray, Vec interceptPoint, Vec normal, Material material) {
        Ray reflected = ray.getReflected(normal, interceptPoint);
        if (this.debug) {
            addDebugString(ray, "reflected ray: %s", reflected);
            ray.addDebug(reflected);
        }
        return resolveRayColor(depth + 1, reflectiveness * material.getReflectiveness(), reflected)
                .mul(material.getReflectiveColor());
    }

    private Color getColorFromObject(int depth, float reflectiveness, Intercept intercept, Ray ray, Matrix objectToWorld, SceneObject sceneObject) {

        Vec interceptPoint = objectToWorld.transform(intercept.interceptPoint);
        Vec normal = objectToWorld.transform(sceneObject.getNormal(ray, intercept));

        if (this.debug) {
            addDebugString(ray, "world intercept: %s world normal: %s", interceptPoint, normal);
        }

        // The light coming from this point can come from different sources:
        // - Diffuse reflection (matte reflection)
        // - Specular reflection (mirror reflection)
        // - Transmitted through the object (e.g. glass)

        // Diffuse reflection
        // For diffuse reflection, for now we only consider light coming directly from light sources
        Material material = sceneObject.getMaterial();
        Color light = scene.getAmbientLight().getColor();
        for (DirectionalLight directionalLight : scene.getLights()) {
            float directionalLightEnergy = max(0, normal.dot(directionalLight.getDirection()));
            if (directionalLightEnergy > 0) {
                // check shadow
                Ray lightRay = directionalLight.getRay(interceptPoint);
                if (this.debug) {
                    addDebugString(ray, "shadow ray: %s", lightRay);
                    ray.addDebug(lightRay);
                }

                if (!interceptsRay(lightRay)) {
                    light = directionalLight.getColor().mulAdd(directionalLightEnergy, light);
                } else {
                    if (this.debug) {
                        addDebugString(ray, "shadow");
                    }
                }
            }
        }

        // Some of this light got absorbed by the material.
        Color objectColor = material.getDiffuseColor(intercept).mul(light);

        if (reflectiveness <= 1e-6) {
            return objectColor;
        }

        if (!material.hasRefraction()) {

            // Specular reflection only
            if (material.getReflectiveness() > 0) {
                Color reflectedLight = getSpecularReflection(depth, reflectiveness, ray, interceptPoint, normal, material);
                objectColor = reflectedLight.mulAdd(material.getReflectiveness(), objectColor);
            }

        } else {

            // Light passing through a surface breaks down to absorbed, transmitted, and reflected light
            // mix according to the coefficient of reflection
            float R0 = pow((1 - material.getIndexOfRefraction()) / (1 + material.getIndexOfRefraction()), 2);
            float reflectionCoeff = R0 + (1 - R0) * pow(1 - abs(Vec.cos(normal, ray.getDirection())), 5);
            if (this.debug) {
                addDebugString(ray, "reflection coeff %f", reflectionCoeff);
            }

            // Specular reflection
            // Ignore internal reflection for now, easily becomes infinite loop
            if (normal.dot(ray.getDirection()) < 0 && material.getReflectiveness() > 0) {
                Color reflectedLight = getSpecularReflection(depth, reflectiveness * reflectionCoeff, ray, interceptPoint, normal, material)
                        .mul(reflectionCoeff);
                objectColor = reflectedLight.mulAdd(reflectiveness * material.getReflectiveness(), objectColor);
            }

            // Light transmitted through the surface
            Ray transmitted = ray.getTransmitted(normal, interceptPoint, material.getIndexOfRefraction());
            if (this.debug) {
                addDebugString(ray, "transmitted ray: %s", transmitted);
            }
            if (transmitted != null) {
                ray.addDebug(transmitted);
                Color transmittedLight = resolveRayColor(depth + 1, reflectiveness * (1 - reflectionCoeff), transmitted);

                objectColor = transmittedLight.mulAdd(1 - reflectionCoeff, objectColor);
            }

        }

        return objectColor;
    }

    void addDebugString(Ray ray, String messageFormat, Object... args) {
        String message = String.format(messageFormat, args);
        ray.addDebug(message);
    }
}
