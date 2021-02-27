package rausku.scenes;

import rausku.geometry.Intercept;

public class SceneIntercept {
    public final int interceptIndex;
    public final Intercept intercept;

    public SceneIntercept(int interceptIndex, Intercept intercept) {
        this.interceptIndex = interceptIndex;
        this.intercept = intercept;
    }

    public boolean isValid() {
        return intercept.isValid();
    }
}
