package rausku.algorithm;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Rand;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.Scene;
import rausku.scenes.SceneIntercept;

import java.util.Random;

public class MonteCarloRayTracer implements RayTracer {

    public static final double MIN_INTENSITY = 1e-6;
    private boolean debug;
    private Scene scene;

    private Random rng = new Random();
    private int lightRayCount = 16;

    public MonteCarloRayTracer(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Color resolveRayColor(Ray ray, int depth) {

        if (depth > 2) {
            return Color.of(0f);
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
            return getColorFromObject(depth, intercept.intercept, ray, intercept.interceptIndex);
        }

        // Specular reflection of the light source itself when nothing is hit
//        for (LightSource light : scene.getLights()) {
//            if (light.intercepts(ray)) {
//                return light.getColor().mul(10);
//            }
//        }

        // nothing hit
        return scene.getAmbientLight().getColor();
    }

    private Color getColorFromObject(int depth, Intercept intercept, Ray ray, int interceptIndex) {

        int index = interceptIndex;

        Matrix objectToWorld = scene.getTransform(index);
        Matrix worldToObject = scene.getInverseTransform(index);
        SceneObject sceneObject = scene.getObject(index);
        Material material = scene.getMaterial(index);

        Vec interceptPoint = ray.apply(intercept.intercept);
        Vec objectNormal = material.getNormal(intercept, sceneObject);

        Vec normal = worldToObject.transposeTransform(objectNormal).toVector().normalize();

        if (this.debug) {
            addDebugString(ray, "world intercept: %s world normal: %s", interceptPoint, normal);
        }

        // The light coming from this point can come from different sources:
        // - Diffuse reflection (matte reflection)
        // - Specular reflection (mirror reflection)
        // - Transmitted through the object (e.g. glass)

        // Diffuse reflection

        Color light = computeAmbientLight(depth, ray, interceptPoint, normal);

        if (debug) {
            addDebugString(ray, "light %s", light);
        }

//        for (LightSource lightSource : scene.getLights()) {
//            float intensity = lightSource.getIntensity(interceptPoint);
//            if (intensity <= MIN_INTENSITY) {
//                // No light from this light source
//                continue;
//            }
//            Ray lightRay = lightSource.getRay(interceptPoint);
//            if (debug) {
//                ray.addDebug(lightRay);
//            }
//            float diffuseReflectionEnergy = normal.dot(lightRay.direction);
//            if (diffuseReflectionEnergy > 0) {
//                // check shadow
//                if (this.debug) {
//                    addDebugString(ray, "shadow ray: %s", lightRay);
//                    ray.addDebug(lightRay);
//                }
//
//                float shadowProbability = getShadowProbability(lightRay);
//                light = lightSource.getColor().mulAdd(intensity * diffuseReflectionEnergy * (1 - shadowProbability), light);
//            }
//        }

        // Some of this light got absorbed by the material.
        Color objectColor = material.getDiffuseColor(intercept).mul(light);

        // Specular reflection

        // Transmitted light

        return objectColor;
    }

    private Color computeAmbientLight(int depth, Ray ray, Vec interceptPoint, Vec normal) {
        Color light = Color.of(0);

        for (int i = 0; i < lightRayCount; i++) {
            Vec side = normal.perpendicular();
            Vec up = Vec.cross(normal, side);

            Vec hemisphere = Rand.cosineHemisphere(rng.nextFloat(), rng.nextFloat());
            Vec randomDirection = Vec.mulAdd(hemisphere.x, side, hemisphere.y, up, hemisphere.z, normal);

            float diffuseReflectionEnergy = hemisphere.z; // = normal.dot(lightRay.direction)

            Ray lightRay = Ray.fromOriginDirection(interceptPoint, randomDirection);
            if (this.debug) {
                ray.addDebug(lightRay);
            }
            light = resolveRayColor(lightRay, depth + 1).mulAdd(diffuseReflectionEnergy / lightRayCount, light);
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
