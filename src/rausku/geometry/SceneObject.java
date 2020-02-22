package rausku.geometry;

import rausku.Material;
import rausku.Ray;
import rausku.math.Vec;

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
