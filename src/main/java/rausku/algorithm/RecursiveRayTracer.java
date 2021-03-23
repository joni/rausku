package rausku.algorithm;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.lighting.LightSource;
import rausku.material.BRDF;
import rausku.material.Material;
import rausku.math.FresnelReflectance;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.Scene;
import rausku.scenes.SceneIntercept;
import rausku.scenes.SceneObjectInstance;

import static rausku.math.FloatMath.abs;

public class RecursiveRayTracer implements RayTracer {

    public static final float MIN_INTENSITY = 1e-6f;
    private final Scene scene;
    private final Params params;

    private final int maxDepth;
    private boolean debug;

    public RecursiveRayTracer(Scene scene, Params params) {
        this.scene = scene;
        this.params = params;
        this.maxDepth = params.maxDepth;
        this.debug = false;
    }

    @Override
    public void setDebug(boolean debug) {
        this.debug = debug;
        scene.setDebug(debug);
    }

    Color resolveRayColor(int depth, Ray ray) {
        if (depth > maxDepth) {
            if (this.debug) {
                addDebugString(ray, "Max depth reached");
            }
            return Color.black();
        }

        SceneIntercept sceneIntercept = scene.getIntercept(ray);
        Intercept intercept = sceneIntercept.intercept;

        if (this.debug) {
            if (sceneIntercept.isValid()) {
                addDebugString(ray, "depth=%d object=%s %s", depth, sceneIntercept.sceneObjectInstance, intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (intercept.isValid()) {
            Color returnedLight = getColorFromObject(depth, sceneIntercept, ray);
            addDebugString(ray, "Returned Light %s", returnedLight);
            return returnedLight;
        }

        // Specular reflection of the light source itself when nothing is hit
        for (LightSource light : scene.getLights()) {
            if (light.intercepts(ray)) {
                return light.getColor();
            }
        }

        // nothing hit
        return Color.black();

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
        return resolveRayColor(depth, ray);
    }

    Color getSpecularReflection(int depth, Ray ray, SceneIntercept intercept, Vec normal, Material material) {

        Matrix localBase = Matrix.orthonormalBasis(normal);
        Matrix localInvert = localBase.transpose();
        Vec localOutgoing = localInvert.transform(ray.direction);

        BRDF.Sample sample = material.getBSDF(intercept.intercept).sample(localOutgoing, 0, 0);
        Vec worldIncidentDirection = localBase.transform(sample.incident).normalize();

        Color f = sample.color;
        if (!f.isBlack()) {
            Ray reflected = Ray.fromOriginDirection(intercept.worldInterceptPoint, worldIncidentDirection);
            if (this.debug) {
                addDebugString(ray, "reflected ray: %s", reflected);
                ray.addDebug(reflected);
            }
            float cosineIncident = abs(sample.incident.y); // = normal.dot(incidentRay.direction)

            return resolveRayColor(depth + 1, reflected).mul(f).mul(cosineIncident);
        } else {
            return Color.black();
        }
    }

    private Color getColorFromObject(int depth, SceneIntercept intercept, Ray ray) {

        SceneObjectInstance sceneObjectInstance = intercept.sceneObjectInstance;
        Matrix worldToObject = sceneObjectInstance.worldToObject;
        Matrix objectToWorld = sceneObjectInstance.objectToWorld;
        SceneObject sceneObject = sceneObjectInstance.object;
        Material material = sceneObjectInstance.material;

        Vec interceptPoint = intercept.worldInterceptPoint; // objectToWorld.transform(intercept.interceptPoint);
        Vec objectNormal = material.getNormal(intercept.intercept, sceneObject);

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
        Color light = Color.black(); //scene.getAmbientLight().getColor();

        for (LightSource lightSource : scene.getLights()) {
            float intensity = lightSource.getIntensity(interceptPoint);
            if (intensity <= MIN_INTENSITY) {
                // No light from this light source
                continue;
            }
            LightSource.Sample sample = lightSource.sample(intercept, 0, 1);
            Ray lightRay = sample.ray;
            if (this.debug) {
                addDebugString(ray, "shadow ray: %s", lightRay);
                ray.addDebug(lightRay);
            }
            float diffuseReflectionEnergy = normal.dot(lightRay.direction);
            if (diffuseReflectionEnergy > 0) {
                // check shadow
                if (!scene.interceptsRay(lightRay)) {
                    Color materialColor = material.getBSDF(intercept.intercept).evaluate(ray.direction, lightRay.direction);
                    light = sample.color.mul(materialColor).mulAdd(diffuseReflectionEnergy / sample.likelihood, light);
                }
            }
        }

        addDebugString(ray, "Direct light %s", light);

        if (!material.hasRefraction()) {

            // Specular reflection only
            Color reflectedLight = getSpecularReflection(depth, ray, intercept, normal, material);
            light = reflectedLight.add(light);

        } else {

            // Light passing through a surface breaks down to absorbed, transmitted, and reflected light
            // mix according to the coefficient of reflection
            float reflectionCoeff = FresnelReflectance.approximation(material.getIndexOfRefraction(), Vec.cos(normal, ray.direction));
            if (this.debug) {
                addDebugString(ray, "reflection coeff %f", reflectionCoeff);
            }

            // Specular reflection
            // Ignore internal reflection for now, easily becomes infinite loop
            if (normal.dot(ray.direction) < 0) {
                Color reflectedLight = getSpecularReflection(depth, ray, intercept, normal, material);
                light = reflectedLight.mulAdd(reflectionCoeff, light);
            }

            // Light transmitted through the surface
            Ray transmitted = ray.getTransmitted(normal, interceptPoint, material.getIndexOfRefraction());
            if (this.debug) {
                addDebugString(ray, "transmitted ray: %s", transmitted);
            }
            if (transmitted != null) {
                ray.addDebug(transmitted);
                Color transmittedLight = resolveRayColor(depth + 1, transmitted);

                light = transmittedLight.mulAdd(1 - reflectionCoeff, light);
            }
        }

        // Some of this light got absorbed by the material.
        return light;
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
