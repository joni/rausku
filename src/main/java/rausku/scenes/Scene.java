package rausku.scenes;

import rausku.geometry.Geometry;
import rausku.geometry.Intercept;
import rausku.lighting.LightSource;
import rausku.math.Matrix;
import rausku.math.Ray;

import java.util.Collection;
import java.util.List;

public class Scene {

    private final SceneDefinition sceneDefinition;

    private boolean debug = false;

    public Scene(SceneDefinition sceneDefinition) {
        this.sceneDefinition = sceneDefinition;
    }

    public Collection<LightSource> getLights() {
        return sceneDefinition.getLights();
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean interceptsRay(Ray ray) {
        List<SceneObjectInstance> objects = sceneDefinition.getObjects();
        for (SceneObjectInstance sceneObjectInstance : objects) {
            Geometry object = sceneObjectInstance.object;
            Matrix transform = sceneObjectInstance.worldToObject;
            Ray transform1 = transform.transform(ray);
            Intercept intercept2 = object.getIntercept(transform1);
            float intercept = intercept2.intercept;
            if (Float.isFinite(intercept) && intercept > 0 && intercept < ray.bound) {
                if (debug) {
                    ray.addDebug(transform1);
                    ray.addDebug(String.format("intercept %s, %s", sceneObjectInstance, intercept2));
                }
                return true;
            }
        }
        if (debug) {
            ray.addDebug("no intercept");
        }
        return false;
    }

    public SceneIntercept getIntercept(Ray ray) {
        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept intercept = Intercept.noIntercept();
        SceneObjectInstance closestSceneObjectInstance = null;

        List<SceneObjectInstance> objects = sceneDefinition.getObjects();
        for (SceneObjectInstance sceneObjectInstance : objects) {
            Geometry object = sceneObjectInstance.object;
            Matrix transform = sceneObjectInstance.worldToObject;
            Ray transformed = transform.transform(ray);
            Intercept objectIntercept = object.getIntercept(transformed);
            float interceptValue = objectIntercept.intercept;
            if (interceptValue > Geometry.INTERCEPT_NEAR && interceptValue < closestIntercept) {
                closestIntercept = interceptValue;
                intercept = objectIntercept;
                closestSceneObjectInstance = sceneObjectInstance;
            }
        }
        return new SceneIntercept(closestSceneObjectInstance, ray.apply(intercept.intercept), intercept);
    }
}
