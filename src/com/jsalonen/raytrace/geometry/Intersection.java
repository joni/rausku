package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Intersection extends SceneObject {

    private final Material material;
    private SceneObject obj1;
    private SceneObject obj2;

    public Intersection(Material material, Cube cube, Sphere sphere) {
        this.material = material;
        obj1 = cube;
        obj2 = sphere;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public float getIntercept(Ray ray) {
        return max(obj1.getIntercept(ray), obj2.getIntercept(ray));
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        float[] intercepts = obj1.getIntercepts(ray);
        float[] intercepts1 = obj2.getIntercepts(ray);
        float[] allIntercepts = {max(intercepts[0], intercepts1[0]), min(intercepts[1], intercepts1[1])};
        return allIntercepts;
    }

    @Override
    public Vec getNormal(Ray ray, Vec interceptPoint) {
        float intercept1 = obj1.getIntercept(ray);
        float intercept2 = obj2.getIntercept(ray);
        float max = max(intercept1, intercept2);
        if (intercept1 == max) {
            return obj1.getNormal(ray, interceptPoint);
        } else if (intercept2 == max) {
            return obj2.getNormal(ray, interceptPoint);
        } else {
            // should not happen
            return null;
        }
    }

}
