package rausku.geometry;

import rausku.Color;
import rausku.Material;
import rausku.Ray;
import rausku.RayTracer;
import rausku.math.Vec;

public class HorizontalPlane extends SceneObject {
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
    public float[] getIntercepts(Ray ray) {
        return new float[]{getIntercept(ray)};
    }

    public float getIntercept(Ray ray) {
        float intercept = (groundLevel - ray.getOrigin().y) / ray.getDirection().y;
        if (intercept > RayTracer.INTERCEPT_NEAR) {
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
