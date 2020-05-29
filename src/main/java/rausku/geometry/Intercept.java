package rausku.geometry;

import rausku.math.Vec;

public class Intercept {

    private static final Intercept NO_INTERCEPT = new Intercept(Float.POSITIVE_INFINITY, Vec.origin(), null);

    public final float intercept;
    public final Vec interceptPoint;
    public final Object info;

    public Intercept(float intercept, Vec interceptPoint, Object info) {
        this.intercept = intercept;
        this.interceptPoint = interceptPoint;
        this.info = info;
    }

    public static Intercept noIntercept() {
        return NO_INTERCEPT;
    }

    public boolean isValid() {
        return Float.isFinite(intercept) && intercept > SceneObject.INTERCEPT_NEAR;
    }

    @Override
    public String toString() {
        return String.format("Intercept{intercept=%s, interceptPoint=%s, info=%s}", intercept, interceptPoint, info);
    }
}