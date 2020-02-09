package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.math.Vec;

public class HorizontalPlane extends SceneObject {
    private float groundLevel;

    public HorizontalPlane(float groundLevel) {
        this.groundLevel = groundLevel;
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        return new float[]{getIntercept(ray)};
    }

    public float getIntercept(Ray ray) {
        float intercept = (groundLevel - ray.getOrigin().y) / ray.getDirection().y;
        if (intercept > 0) {
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
        return Material.plastic(Color.of(.24f, .5f, .2f), .3f);
    }
}
