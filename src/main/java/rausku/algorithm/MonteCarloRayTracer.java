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
    public static final int MAX_DEPTH = 2;
    private boolean debug;
    private Scene scene;

    private int lightRayCount = 16;

    public MonteCarloRayTracer(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Color resolveRayColor(Ray ray, int depth) {

        if (depth > MAX_DEPTH) {
            return Color.black();
        }

        SceneIntercept intercept = scene.getIntercept(ray);

        if (this.debug) {
            if (intercept.isValid()) {
                addDebugString(ray, "depth=%d intercept=%s", depth, intercept);
            } else {
                addDebugString(ray, "depth=%d no intercept", depth);
            }
        }

        if (intercept.isValid()) {
            return getColorFromObject(depth, intercept, ray);
        }

        // Nothing hit in scene - check light directly from sources that have no geometry
        Color totalLight = Color.black();
        for (LightSource light : scene.getLights()) {
            if (light.intercepts(ray)) {
                totalLight = totalLight.add(light.getColor());
            }
        }

        return totalLight;
    }

    private Color getColorFromObject(int depth, SceneIntercept sceneIntercept, Ray ray) {

        Intercept intercept = sceneIntercept.intercept;
        Matrix objectToWorld = sceneIntercept.sceneObjectInstance.transform;
        Matrix worldToObject = sceneIntercept.sceneObjectInstance.inverseTransform;
        SceneObject sceneObject = sceneIntercept.sceneObjectInstance.object;
        Material material = sceneIntercept.sceneObjectInstance.material;

        Vec interceptPoint = sceneIntercept.worldInterceptPoint;
        Vec objectNormal = material.getNormal(intercept, sceneObject);

        Vec worldNormal = worldToObject.transposeTransform(objectNormal).toVector().normalize();

        Matrix localBase = Matrix.orthonormalBasis(worldNormal);
        Matrix localInvert = localBase.transpose();

        Vec localOutgoing = localInvert.transform(ray.direction);

        if (this.debug) {
            addDebugString(ray, "world intercept: %s world normal: %s", interceptPoint, worldNormal);
            addDebugString(ray, "local outgoing: %s", localOutgoing);
        }

        Random rng = ThreadLocalRandom.current();

        BRDF bsdf = material.getBSDF(intercept);

        // sample light coming directly from light source
        Color directLight = sampleDirectLighting(sceneIntercept, ray, worldNormal, rng, bsdf);

        // sample light from BSDF
        Color light = Color.black();
        int bsdfSamples = 16;

        for (int i = 0; i < bsdfSamples; i++) {

            BRDF.Sample sample = bsdf.sample(localOutgoing, rng.nextFloat(), rng.nextFloat());

            Vec globalIncidentDirection = localBase.transform(sample.incident).normalize();

            float cosineIncident = abs(sample.incident.y); // = normal.dot(incidentRay.direction)

            Ray incidentRay = Ray.fromOriginDirection(interceptPoint, globalIncidentDirection);

            if (this.debug) {
                addDebugString(ray, "local incident: %s", sample.incident);
                ray.addDebug(incidentRay);
            }

            Color incidentRadiance = resolveRayColor(incidentRay, depth + 1);

            light = incidentRadiance
                    .mul(sample.color)
                    .mulAdd(cosineIncident / sample.likelihood, light);
        }

        Color totalLight = light.mulAdd(1f / bsdfSamples, directLight);

        if (debug) {
            addDebugString(ray, "light %s", totalLight);
        }

        return totalLight;
    }

    private Color sampleDirectLighting(SceneIntercept intercept, Ray ray, Vec normal, Random rng, BRDF bsdf) {
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
                                .mul(bsdf.evaluate(ray.direction, lightRay.direction))
                                .mulAdd(intensity * cosineIncident / lightRayCount, light);
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
