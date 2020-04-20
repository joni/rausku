package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

public abstract class SceneObject {
    public static final double INTERCEPT_NEAR = 1e-3;

    public abstract Vec getNormal(Ray ray, Intercept interceptPoint);

    public abstract Material getMaterial();

    public abstract float getIntercept(Ray ray);

    public Intercept getIntercept2(Ray ray) {
        float intercept = getIntercept(ray);
        return new Intercept(intercept, ray.apply(intercept), null);
    }

    public abstract float[] getIntercepts(Ray ray);
}
