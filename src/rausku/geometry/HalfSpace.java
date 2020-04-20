package rausku.geometry;

import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

public class HalfSpace implements CSGObject, SceneObject {
    private Vec v;
    private Material material;

    public HalfSpace(Vec v) {
        this(v, Material.plastic(Color.of(.24f, .5f, .2f), 0));
    }

    public HalfSpace(Vec v, Material material) {
        this.v = v;
        this.material = material;
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        return new float[]{getIntercept0(ray)};
    }

    public Intercept getIntercept(Ray ray) {
        float intercept = getIntercept0(ray);
        return new Intercept(intercept, ray.apply(intercept), null);
    }

    private float getIntercept0(Ray ray) {
        // v.(dt+o) = 0  <=> (v.d)t = - v.o  <=> t = -v.o/v.d
        float intercept = -Vec.dot(v, ray.getOrigin()) / Vec.dot(v, ray.getDirection());
        if (intercept > SceneObject.INTERCEPT_NEAR) {
            return intercept;
        } else {
            return Float.NaN;
        }
    }

    public Vec getNormal(Ray ray, Intercept point) {
        return v;
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
