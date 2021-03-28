package rausku.scenes;

import rausku.geometry.Intercept;
import rausku.math.Vec;

public record SceneIntercept(SceneObjectInstance sceneObjectInstance, Vec worldInterceptPoint, Intercept intercept) {

    public boolean isValid() {
        return intercept.isValid();
    }

}
