package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.math.Vec;

public class Intercept {
    public final float intercept;
    public final Vec interceptPoint;
    public final Object info;

    public Intercept(float intercept, Vec interceptPoint, Object info) {
        this.intercept = intercept;
        this.interceptPoint = interceptPoint;
        this.info = info;
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
