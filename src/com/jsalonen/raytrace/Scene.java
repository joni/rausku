package com.jsalonen.raytrace;

import com.jsalonen.raytrace.geometry.Intercept;
import com.jsalonen.raytrace.geometry.SceneObject;
import com.jsalonen.raytrace.lighting.AmbientLight;
import com.jsalonen.raytrace.lighting.DirectionalLight;
import com.jsalonen.raytrace.math.Vec;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public abstract class Scene {

    public static final double INTERCEPT_NEAR = 1e-3;
    protected List<SceneObject> objects = new ArrayList<>();
    protected Camera camera = Camera.initialCamera();

    protected DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, .5f).normalize(), Color.of(1, 1, 1));
    //    DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, 0).normalize(), Color.of(1, 1, 1));
    AmbientLight ambientLight = new AmbientLight(Color.of(.1f, .1f, .1f));


    boolean interceptsRay(Ray ray) {
        return objects.stream().anyMatch(o -> o.getIntercept(ray) > INTERCEPT_NEAR);
    }

    Color resolveRayColorDebug(float reflectiveness, Ray ray, boolean debug) {
        float closestIntercept = Float.POSITIVE_INFINITY;
        SceneObject closestObject = null;

        for (SceneObject object : objects) {
            float intercept = object.getIntercept(ray);
            if (intercept > INTERCEPT_NEAR && intercept < closestIntercept) {
                closestIntercept = intercept;
                closestObject = object;
            }
        }

        if (closestObject != null) {
            if (debug) {
                System.out.printf("object %s\n", closestObject);
            }
            int hashCode = closestObject.hashCode();
            return Color.of((0xff & (hashCode >>> 16)) / 255f, (0xff & (hashCode >>> 8)) / 255f, (0xff & (hashCode)) / 255f);
        }

        return Color.of(0, 0, 0);
    }

    Color resolveRayColor(float reflectiveness, Ray ray) {

        float closestIntercept = Float.POSITIVE_INFINITY;
        SceneObject closestObject = null;
        Intercept interceptInfo = null;

        for (SceneObject object : objects) {
            Intercept intercept2 = object.getIntercept2(ray);
            float intercept = intercept2.intercept;
            if (intercept > INTERCEPT_NEAR && intercept < closestIntercept) {
                closestIntercept = intercept;
                closestObject = object;
                interceptInfo = intercept2;
            }
        }

        if (closestObject != null) {
            return getColorFromObject(reflectiveness, interceptInfo, ray, closestObject);
        }

        if (Vec.cos(directionalLight.getDirection(), ray.getDirection()) > .99) {
            return Color.of(10f, 10f, 10f);
        }

        // nothing hit
        return ambientLight.getColor().copy();
    }

    private Color getColorFromObject(float reflectiveness, Intercept intercept, Ray ray, SceneObject sceneObject) {

        Vec interceptPoint = intercept.interceptPoint;

        Vec normal = sceneObject.getNormal(ray, intercept);

        Material material = sceneObject.getMaterial();
        Color light = ambientLight.getColor().copy();

        float directionalLightEnergy = max(0, normal.dot(directionalLight.getDirection()));
        if (directionalLightEnergy > 0) {
            Ray lightRay = directionalLight.getRay(interceptPoint);
            if (!interceptsRay(lightRay)) {
                light.add(directionalLight.getColor().copy().mul(directionalLightEnergy));
            }
        }

        Color diffuseColor = material.getDiffuseColor(intercept).copy().mul(light);

        if (reflectiveness <= 1e-6) {
            return diffuseColor;
        }

        if (material.getReflectiveness() > 0) {
            Ray reflected = ray.getReflected(normal, interceptPoint);
            Color reflectedLight = resolveRayColor(reflectiveness * material.getReflectiveness(), reflected);
            reflectedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor.add(reflectedLight);
        }

        if (material.hasRefraction()) {
            Ray refracted = ray.getRefracted(normal, interceptPoint, material.getIndexOfRefraction());
            Color refractedLight = resolveRayColor(reflectiveness, refracted);
//            refractedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor.add(refractedLight);
        }

        return diffuseColor;
    }

    public Camera getCamera() {
        return camera;
    }
}
