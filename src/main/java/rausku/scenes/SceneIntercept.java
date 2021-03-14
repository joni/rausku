package rausku.scenes;

import rausku.geometry.Intercept;
import rausku.math.Vec;

public class SceneIntercept {
    public final SceneObjectInstance sceneObjectInstance;
    public final Vec worldInterceptPoint;
    public final Intercept intercept;

    public SceneIntercept(SceneObjectInstance sceneObjectInstance, Vec worldInterceptPoint, Intercept intercept) {
        this.sceneObjectInstance = sceneObjectInstance;
        this.worldInterceptPoint = worldInterceptPoint;
        this.intercept = intercept;
    }

    public boolean isValid() {
        return intercept.isValid();
    }
}
