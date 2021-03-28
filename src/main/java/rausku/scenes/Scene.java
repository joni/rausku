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

    public Scene(SceneDefinition sceneDefinition) {
        this.sceneDefinition = sceneDefinition;
    }

    public Collection<LightSource> getLights() {
        return sceneDefinition.getLights();
    }

    public boolean interceptsRay(Ray ray) {
        List<SceneObjectInstance> objects = sceneDefinition.getObjects();
        for (SceneObjectInstance sceneObjectInstance : objects) {
            Geometry object = sceneObjectInstance.object;
            Matrix transform = sceneObjectInstance.worldToObject;
            Ray transform1 = transform.transform(ray);
            if (object.hasIntercept(transform1)) {
                return true;
            }
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
