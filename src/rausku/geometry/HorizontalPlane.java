package rausku.geometry;

import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

public class HorizontalPlane implements CSGObject, SceneObject {
    private float groundLevel;
    private Material material;

    public HorizontalPlane(float groundLevel) {
        this(groundLevel, Material.plastic(Color.of(.24f, .5f, .2f), 0));
    }

    public HorizontalPlane(float groundLevel, Material material) {
        this.groundLevel = groundLevel;
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
        float intercept = (groundLevel - ray.getOrigin().y) / ray.getDirection().y;
        if (intercept > SceneObject.INTERCEPT_NEAR) {
            return intercept;
        } else {
            return Float.NaN;
        }
    }

    public Vec getNormal(Ray ray, Intercept point) {
        return Vec.of(0, 1, 0);
    }

    @Override
    public Material getMaterial() {
        return material;
    }
}
