package rausku.algorithm;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.lighting.LightSource;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.Scene;
import rausku.scenes.SceneIntercept;

import static rausku.math.FloatMath.abs;
import static rausku.math.FloatMath.pow;

public class RecursiveRayTracer implements RayTracer {

    public static final float MIN_INTENSITY = 1e-6f;
    private final Scene scene;
    private final Params params;

    private final int maxDepth;
    private boolean softShadows;
    private boolean debug;

    public RecursiveRayTracer(Scene scene, Params params) {
        this.scene = scene;
        this.params = params;
        this.maxDepth = params.maxDepth;
        this.softShadows = params.softShadows;
        this.debug = false;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
        scene.setDebug(debug);
    }

    Color resolveRayColor(int depth, float reflectiveness, Ray ray) {


        if (depth > maxDepth) {
            if (this.debug) {
                addDebugString(ray, "Max depth reached");
            }
            return scene.getAmbientLight().getColor();
        }

        SceneIntercept sceneIntercept = scene.getIntercept(ray);
        Intercept intercept = sceneIntercept.intercept;
        int index = sceneIntercept.interceptIndex;

        if (this.debug) {
            if (index >= 0) {
                addDebugString(ray, "depth=%d object=%d %s", depth, index, intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (intercept.isValid()) {
            return getColorFromObject(depth, reflectiveness, intercept, ray, index);
        }

        // Specular reflection of the light source itself when nothing is hit
        for (LightSource light : scene.getLights()) {
            if (light.intercepts(ray)) {
                return light.getColor().mul(10);
            }
        }

        // nothing hit
        return scene.getAmbientLight().getColor();

//        BoundingBox bbox = new BoundingBox(Vec.origin(), Vec.point(+2,+2,+2));
//        float[] intercepts = bbox.getIntercepts(ray);
//        if (intercepts.length==0) {
//            return scene.getAmbientLight().getColor();
//        }
//        Vec interceptPoint = ray.apply(intercepts[1]);
//        float x, y, z;
//        x = limit(interceptPoint.x, 2);
//        y = limit(interceptPoint.y, 2);
//        z = limit(interceptPoint.z, 2);
//        Vec newOrigin = Vec.point(x, y, z);
//        return resolveRayColor(depth+1, reflectiveness, ray.withOrigin(newOrigin));
    }

    private float limit(float value, float limit) {
        if (value >= limit) return value - limit;
        else if (value <= 1e-6) return value + limit;
        else return value;
    }

    @Override
    public Color resolveRayColor(Ray ray, int depth) {
        return resolveRayColor(depth, 1f, ray);
    }

    Color getSpecularReflection(int depth, float reflectiveness, Ray ray, Intercept intercept, Vec normal, Material material) {
        Ray reflected = ray.getReflected(normal, intercept.interceptPoint);
        if (this.debug) {
            addDebugString(ray, "reflected ray: %s", reflected);
            ray.addDebug(reflected);
        }
        return resolveRayColor(depth + 1, reflectiveness * material.getReflectiveness(), reflected)
                .mul(material.getReflectiveColor(intercept));
    }

    private Color getColorFromObject(int depth, float reflectiveness, Intercept intercept, Ray ray, int index) {

        Matrix objectToWorld = scene.getTransform(index);
        Matrix worldToObject = scene.getInverseTransform(index);
        SceneObject sceneObject = scene.getObject(index);
        Material material = scene.getMaterial(index);

        Vec interceptPoint = ray.apply(intercept.intercept); // objectToWorld.transform(intercept.interceptPoint);
        Vec objectNormal = material.getNormal(intercept, sceneObject);

//        if (Vec.dot(objectNormal, ray.direction) > 0) {
//            objectNormal = objectNormal.mul(-1);
//        }
        Vec normal = worldToObject.transposeTransform(objectNormal).toVector().normalize();

        if (this.debug) {
            addDebugString(ray, "world intercept: %s world normal: %s", interceptPoint, normal);
        }

        // The light coming from this point can come from different sources:
        // - Diffuse reflection (matte reflection)
        // - Specular reflection (mirror reflection)
        // - Transmitted through the object (e.g. glass)

        // Diffuse reflection
        // For diffuse reflection, for now we only consider light coming directly from light sources
        Color light = scene.getAmbientLight().getColor();

        for (LightSource lightSource : scene.getLights()) {
            float intensity = lightSource.getIntensity(interceptPoint);
            if (intensity <= MIN_INTENSITY) {
                // No light from this light source
                continue;
            }
            Ray lightRay = lightSource.sampleRay(intercept);
            if (debug) {
                ray.addDebug(lightRay);
            }
            float diffuseReflectionEnergy = normal.dot(lightRay.direction);
            if (diffuseReflectionEnergy > 0) {
                // check shadow
                if (this.debug) {
                    addDebugString(ray, "shadow ray: %s", lightRay);
                    ray.addDebug(lightRay);
                }

                float shadowProbability = getShadowProbability(lightRay);
                light = lightSource.getColor().mulAdd(intensity * diffuseReflectionEnergy * (1 - shadowProbability), light);
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
                Color reflectedLight = getSpecularReflection(depth, reflectiveness, ray, intercept, normal, material);
                objectColor = reflectedLight.mulAdd(material.getReflectiveness(), objectColor);
            }

        } else {

            // Light passing through a surface breaks down to absorbed, transmitted, and reflected light
            // mix according to the coefficient of reflection
            float R0 = pow((1 - material.getIndexOfRefraction()) / (1 + material.getIndexOfRefraction()), 2);
            float reflectionCoeff = R0 + (1 - R0) * pow(1 - abs(Vec.cos(normal, ray.direction)), 5);
            if (this.debug) {
                addDebugString(ray, "reflection coeff %f", reflectionCoeff);
            }

            // Specular reflection
            // Ignore internal reflection for now, easily becomes infinite loop
            if (normal.dot(ray.direction) < 0 && material.getReflectiveness() > 0) {
                Color reflectedLight = getSpecularReflection(depth, reflectiveness * reflectionCoeff, ray, intercept, normal, material)
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

    private float getShadowProbability(Ray lightRay) {

        if (!softShadows) {
            if (scene.interceptsRay(lightRay)) {
                return 1;
            } else {
                return 0;
            }
        } else {

            int shadowRays = 4;
            int shadowCount = 0;

            for (int i = 0; i < shadowRays; i++) {
                Ray newLightRay = lightRay.jitterDirection();
                if (scene.interceptsRay(newLightRay)) {
                    shadowCount++;
                }
            }

            return (float) shadowCount / shadowRays;
        }
    }

    void addDebugString(Ray ray, String messageFormat, Object... args) {
        String message = String.format(messageFormat, args);
        ray.addDebug(message);
    }

    public static class Params {
        private int maxDepth = 10;
        private boolean softShadows = false;

        public Params withMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public Params withSoftShadows(boolean softShadows) {
            this.softShadows = softShadows;
            return this;
        }
    }
}
