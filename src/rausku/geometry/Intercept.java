package rausku.geometry;

import rausku.math.Vec;

public class Intercept {

    private static final Intercept NO_INTERCEPT = new Intercept(Float.NaN, Vec.origin(), null);

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
        return Float.isFinite(intercept);
    }

    @Override
    public String toString() {
        return "Intercept{" +
                "intercept=" + intercept +
                ", interceptPoint=" + interceptPoint +
                ", info=" + info +
                '}';
    }
}
