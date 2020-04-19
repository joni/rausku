package rausku;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.math.Matrix;
import rausku.math.Vec;

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
        for (int i = 0; i < scene.objects.size(); i++) {
            SceneObject object = scene.objects.get(i);
            Matrix transform = scene.inverseTransforms.get(i);
            Ray transform1 = transform.transform(ray);
            Intercept intercept2 = object.getIntercept2(transform1);
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
            return scene.ambientLight.getColor();
        }

        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept intercept = null;
        int index = -1;

        for (int i = 0; i < scene.objects.size(); i++) {
            SceneObject object = scene.objects.get(i);
            Matrix transform = scene.inverseTransforms.get(i);
            Ray transform1 = transform.transform(ray);
            Intercept objectIntercept = object.getIntercept2(transform1);
            float interceptValue = objectIntercept.intercept;
            if (interceptValue > INTERCEPT_NEAR && interceptValue < closestIntercept) {
                index = i;
                closestIntercept = interceptValue;
                intercept = objectIntercept;
            }
        }

        if (this.debug) {
            if (index >= 0) {
                addDebugString(ray, "depth=%d object=%d[%s] %s", depth, index, scene.objects.get(index), intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (intercept != null) {
            return getColorFromObject(depth, reflectiveness, intercept, ray, scene.transforms.get(index), scene.objects.get(index));
        }

        // Specular reflection of the light source itself when nothing is hit
        if (Vec.cos(scene.directionalLight.getDirection(), ray.getDirection()) > .99) {
            return scene.directionalLight.getColor().mul(10);
        }

        // nothing hit
        return scene.ambientLight.getColor();
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
                .mul(material.getReflectiveness())
                .mul(material.getReflectiveColor());
    }

    void addDebugString(Ray ray, String messageFormat, Object... args) {
        String message = String.format(messageFormat, args);
        ray.addDebug(message);
        System.out.println(message);
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
        Color light = scene.ambientLight.getColor();
        float directionalLightEnergy = max(0, normal.dot(scene.directionalLight.getDirection()));
        if (directionalLightEnergy > 0) {
            // check shadow
            Ray lightRay = scene.directionalLight.getRay(interceptPoint);
            if (this.debug) {
                addDebugString(ray, "shadow ray: %s", lightRay);
                ray.addDebug(lightRay);
            }

            if (!interceptsRay(lightRay)) {
                light = light.add(scene.directionalLight.getColor().mul(directionalLightEnergy));
            } else {
                if (this.debug) {
                    addDebugString(ray, "shadow");
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
                objectColor = objectColor.add(reflectedLight);
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
                objectColor = objectColor.add(reflectedLight);
            }

            // Light transmitted through the surface
            Ray transmitted = ray.getTransmitted(normal, interceptPoint, material.getIndexOfRefraction());
            if (this.debug) {
                addDebugString(ray, "transmitted ray: %s", transmitted);
            }
            if (transmitted != null) {
                ray.addDebug(transmitted);
                Color transmittedLight = resolveRayColor(depth + 1, reflectiveness * (1 - reflectionCoeff), transmitted)
                        .mul(1 - reflectionCoeff);

                objectColor = objectColor.add(transmittedLight);
            }

        }

        return objectColor;
    }
}
