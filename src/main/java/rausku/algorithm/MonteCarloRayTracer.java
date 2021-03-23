package rausku.algorithm;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.lighting.LightSource;
import rausku.material.BRDF;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.Scene;
import rausku.scenes.SceneIntercept;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static rausku.math.FloatMath.abs;

public class MonteCarloRayTracer implements RayTracer {

    public static final double MIN_INTENSITY = 1e-6;
    public static final int MAX_DEPTH = 4;
    private boolean debug;
    private Scene scene;

    private int lightRayCount = 1;
    private int bsdfSamples = 1;
    private int pathSamples = 16;


    public MonteCarloRayTracer(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Color resolveRayColor(Ray ray, int depth) {

        SceneIntercept intercept = scene.getIntercept(ray);

        if (this.debug) {
            if (intercept.isValid()) {
                addDebugString(ray, "depth=%d intercept=%s", depth, intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (!intercept.isValid()) {
            // Nothing hit in scene - check light directly from sources that have no geometry
            return getEnvironmentLight(ray);
        } else {
            return traceFirstPathSegment(depth, intercept, ray);
        }
    }

    private Color extendPath(boolean isSpecular, Ray ray, int depth, float scalar) {

        SceneIntercept intercept = scene.getIntercept(ray);

        if (this.debug) {
            if (intercept.isValid()) {
                addDebugString(ray, "depth=%d scalar=%f intercept=%s", depth, scalar, intercept);
            } else {
                addDebugString(ray, "depth=%d scalar=%f intercept=none", depth, scalar);
            }
        }

        if (intercept.isValid()) {
            return tracePathSegment(depth, intercept, ray, isSpecular, scalar);
        } else if (isSpecular) {
            return getEnvironmentLight(ray);
        } else {
            return Color.black();
        }
    }

    private Color traceFirstPathSegment(int depth, SceneIntercept sceneIntercept, Ray ray) {

        Intercept intercept = sceneIntercept.intercept;
        Matrix worldToObject = sceneIntercept.sceneObjectInstance.worldToObject;
        SceneObject sceneObject = sceneIntercept.sceneObjectInstance.object;
        Material material = sceneIntercept.sceneObjectInstance.material;

        Vec objectNormal = material.getNormal(intercept, sceneObject);

        Vec worldNormal = worldToObject.transposeTransform(objectNormal).toVector().normalize();

        if (sceneObject instanceof LightSource) {
            var lightSource = (LightSource) sceneObject;
            return lightSource.getColor().mul(abs(Vec.dot(ray.direction, worldNormal)));
        }

        if (depth < MAX_DEPTH) {
            Color totalLight = Color.black();
            for (int i = 0; i < pathSamples; i++) {
                totalLight = totalLight.add(tracePathSegment(depth + 1, sceneIntercept, ray, false, 1f / pathSamples));
            }
            return totalLight.div(pathSamples);
        } else {
            return Color.black();
        }
    }

    private Color tracePathSegment(int depth, SceneIntercept sceneIntercept, Ray ray, boolean isSpecular, float scalar) {

        Intercept intercept = sceneIntercept.intercept;
        Matrix worldToObject = sceneIntercept.sceneObjectInstance.worldToObject;
        Matrix objectToWorld = sceneIntercept.sceneObjectInstance.objectToWorld;
        SceneObject sceneObject = sceneIntercept.sceneObjectInstance.object;
        Material material = sceneIntercept.sceneObjectInstance.material;

        Vec interceptPoint = sceneIntercept.worldInterceptPoint;
        Vec objectNormal = material.getNormal(intercept, sceneObject);

        Vec worldNormal = worldToObject.transposeTransform(objectNormal).toVector().normalize();

        if (sceneObject instanceof LightSource) {
            if (isSpecular) {
                var lightSource = (LightSource) sceneObject;
                return lightSource.getColor().mul(abs(Vec.dot(ray.direction, worldNormal)));
            } else {
                return Color.black();
            }
        }

        Matrix shadingToGlobal = Matrix.orthonormalBasis(worldNormal);
        Matrix globalToShading = shadingToGlobal.transpose();

        Vec localOutgoing = globalToShading.transform(ray.direction);

        if (this.debug) {
            addDebugString(ray, "world intercept: %s world normal: %s", interceptPoint, worldNormal);
            addDebugString(ray, "local outgoing: %s", localOutgoing);
        }

        BRDF bsdf = material.getBSDF(intercept);

        // sample light coming directly from light source
        Color directLight = sampleDirectLighting(sceneIntercept, ray, worldNormal, bsdf, localOutgoing, globalToShading);

        Color totalLight;
        if (depth < MAX_DEPTH && scalar > 1e-4f) {
            // extend the path to sample reflected light
            Color light = extendPath(depth, ray, interceptPoint, shadingToGlobal, localOutgoing, bsdf, scalar);
            totalLight = directLight.add(light);
        } else {
            totalLight = directLight;
        }

        if (debug) {
            addDebugString(ray, "light %s", totalLight);
        }

        return totalLight;
    }

    private Color extendPath(int depth, Ray ray, Vec interceptPoint, Matrix shadingToGlobal, Vec
            localOutgoing, BRDF bsdf, float scalar) {
        Random rng = ThreadLocalRandom.current();
        Color light = Color.black();
        for (int i = 0; i < bsdfSamples; i++) {

            BRDF.Sample sample = bsdf.sample(localOutgoing, rng.nextFloat(), rng.nextFloat());

            Vec globalIncidentDirection = shadingToGlobal.transform(sample.incident).normalize();

            float cosineIncident = abs(sample.incident.y); // = normal.dot(incidentRay.direction)
            var beta = cosineIncident / sample.likelihood;

            Ray incidentRay = Ray.fromOriginDirection(interceptPoint, globalIncidentDirection);

            if (this.debug) {
                addDebugString(ray, "local incident: %s", sample.incident);
                ray.addDebug(incidentRay);
            }

            // TODO _specular_ sample needs special treatment
            Color incidentRadiance = extendPath(sample.isSpecular, incidentRay, depth + 1, scalar * beta * sample.color.norm());

            if (this.debug) {
                addDebugString(ray, "beta: %f sample.color: %s cosThetaI: %f likelihood: %f", beta, sample.color, cosineIncident, sample.likelihood);
            }

            light = incidentRadiance
                    .mul(sample.color)
                    .mulAdd(beta, light);
        }
        return light.div(bsdfSamples);
    }

    private Color getEnvironmentLight(Ray ray) {
        Color totalLight = Color.black();
        for (LightSource light : scene.getLights()) {
            if (light.intercepts(ray)) {
                totalLight = totalLight.add(light.getColor());
            }
        }
        return totalLight;
    }

    private Color sampleDirectLighting(SceneIntercept intercept, Ray ray, Vec normal, BRDF bsdf, Vec
            localOutgoing, Matrix globalToShading) {
        Random rng = ThreadLocalRandom.current();
        Color light = Color.black();

        Vec interceptPoint = intercept.worldInterceptPoint;
        for (LightSource lightSource : scene.getLights()) {
            float intensity = lightSource.getIntensity(interceptPoint);
            if (intensity <= MIN_INTENSITY) {
                // No light from this light source
                continue;
            }

            for (int i = 0; i < lightRayCount; i++) {
                LightSource.Sample sample = lightSource.sample(intercept, rng.nextFloat(), rng.nextFloat());
                Ray lightRay = sample.ray;
                float cosineIncident = normal.dot(lightRay.direction);
                if (this.debug) {
                    addDebugString(ray, "shadow ray: %f %s", cosineIncident, lightRay);
//                    ray.addDebug(lightRay);
                }
                if (cosineIncident > 0) {
                    boolean shadow = scene.interceptsRay(lightRay);
                    if (!shadow) {
                        Color incidentRadiance = lightSource.getColor();
                        light = incidentRadiance
                                .mul(bsdf.evaluate(localOutgoing, globalToShading.transform(lightRay.direction)))
                                .mulAdd(intensity * cosineIncident / (sample.likelihood * lightRayCount), light);
                    }
                }
            }
        }
        if (this.debug) {
            addDebugString(ray, "direct light %s", light);
        }
        return light;
    }

    @Override
    public void setDebug(boolean enable) {
        this.debug = enable;
        scene.setDebug(true);
    }

    void addDebugString(Ray ray, String messageFormat, Object... args) {
        if (this.debug) {
            String message = String.format(messageFormat, args);
            ray.addDebug(message);
        }
    }
}
