package rausku.scenes;

import rausku.geometry.Intercept;
import rausku.math.Vec;

public class SceneIntercept {
    public final int objectIndex;
    public final Vec worldInterceptPoint;
    public final Intercept intercept;

    public SceneIntercept(int objectIndex, Vec worldInterceptPoint, Intercept intercept) {
        this.objectIndex = objectIndex;
        this.worldInterceptPoint = worldInterceptPoint;
        this.intercept = intercept;
    }

    public boolean isValid() {
        return intercept.isValid();
    }
}
