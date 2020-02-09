package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.math.Vec;

public abstract class SceneObject {
    public abstract Vec getNormal(Ray ray, Intercept interceptPoint);

    public abstract Material getMaterial();

    public abstract float getIntercept(Ray ray);

    public Intercept getIntercept2(Ray ray) {
        float intercept = getIntercept(ray);
        return new Intercept(intercept, ray.apply(intercept), null);
    }

    public abstract float[] getIntercepts(Ray ray);
}
