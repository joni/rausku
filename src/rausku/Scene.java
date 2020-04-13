package rausku;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;

public abstract class Scene {

    public static final double INTERCEPT_NEAR = 1e-3;

    protected AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));
    protected DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f));

    protected Camera camera = Camera.initialCamera();
    private List<Matrix> transforms = new ArrayList<>();
    private List<Matrix> inverseTransforms = new ArrayList<>();
    private List<SceneObject> objects = new ArrayList<>();

    boolean debug = false;

    protected void addObject(Matrix transform, SceneObject object) {
        transforms.add(transform);
        inverseTransforms.add(transform.inverse());
        objects.add(object);
    }

    protected void addObject(SceneObject object) {
        transforms.add(Matrix.eye());
        inverseTransforms.add(Matrix.eye());
        objects.add(object);
    }

    boolean interceptsRay(Ray ray) {
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = inverseTransforms.get(i);
            Ray transform1 = transform.transform(ray);
            Intercept intercept2 = object.getIntercept2(transform1);
            float intercept = intercept2.intercept;
            if (debug) {
//                System.out.printf("light ray %s\nintercept %d, %s\n", transform1, i, intercept2);
            }
            if (Float.isFinite(intercept) && intercept > 0) {
                return true;
            }
        }
        return false;
    }

    Color resolveRayColor(float reflectiveness, Ray ray) {
        return resolveRayColor(0, reflectiveness, ray);
    }

    Color resolveRayColor(int depth, float reflectiveness, Ray ray) {

        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept intercept = null;
        int index = -1;

        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = inverseTransforms.get(i);
            Ray transform1 = transform.transform(ray);
            Intercept objectIntercept = object.getIntercept2(transform1);
            float interceptValue = objectIntercept.intercept;
            if (interceptValue > INTERCEPT_NEAR && interceptValue < closestIntercept) {
                index = i;
                closestIntercept = interceptValue;
                intercept = objectIntercept;
            }
        }

        if (debug) {
            if (index >= 0) {
                System.out.printf("depth=%d object=%d[%s] %s\n", depth, index, objects.get(index), intercept);
            } else {
                System.out.printf("depth=%d no intercept\n", depth);
            }
        }

        if (intercept != null) {
            return getColorFromObject(depth, reflectiveness, intercept, ray, transforms.get(index), objects.get(index));
        }

        if (Vec.cos(directionalLight.getDirection(), ray.getDirection()) > .99) {
            return Color.of(10f, 10f, 10f);
        }

        // nothing hit
        return ambientLight.getColor();
    }

    private Color getColorFromObject(int depth, float reflectiveness, Intercept intercept, Ray ray, Matrix objectToWorld, SceneObject sceneObject) {

        Vec interceptPoint = objectToWorld.transform(intercept.interceptPoint);
        Vec normal = objectToWorld.transform(sceneObject.getNormal(ray, intercept));

        if (debug) {
            System.out.printf("world intercept: %s world normal: %s\n", interceptPoint, normal);
        }

        Material material = sceneObject.getMaterial();
        Color light = ambientLight.getColor();

        float directionalLightEnergy = max(0, normal.dot(directionalLight.getDirection()));
        if (directionalLightEnergy > 0) {
//             check shadow
            Ray lightRay = directionalLight.getRay(interceptPoint);
            if (debug) {
                System.out.printf("shadow ray: %s\n", lightRay);
            }

            if (!interceptsRay(lightRay)) {
                light = light.add(directionalLight.getColor().mul(directionalLightEnergy));
            } else {
                if (debug) {
                    System.out.println("shadow");
                }
            }
        }

        Color diffuseColor = material.getDiffuseColor(intercept).mul(light);

        if (reflectiveness <= 1e-6) {
            return diffuseColor;
        }

        if (material.getReflectiveness() > 0) {
            Ray reflected = ray.getReflected(normal, interceptPoint);
            if (debug) {
                System.out.printf("reflected ray: %s\n", reflected);
            }
            Color reflectedLight = resolveRayColor(depth + 1, reflectiveness * material.getReflectiveness(), reflected)
                    .mul(material.getReflectiveness())
                    .mul(material.getReflectiveColor());
            diffuseColor = diffuseColor.add(reflectedLight);
        }

        if (material.hasRefraction()) {
            Ray refracted = ray.getRefracted(normal, interceptPoint, material.getIndexOfRefraction());
            if (debug) {
                System.out.printf("refracted ray: %s\n", refracted);
            }
            Color refractedLight = resolveRayColor(depth + 1, reflectiveness, refracted);
//            refractedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor = diffuseColor.add(refractedLight);
        }

        return diffuseColor;
    }

    public Camera getCamera() {
        return camera;
    }
}
