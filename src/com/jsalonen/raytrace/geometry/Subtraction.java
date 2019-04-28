package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;

public class Subtraction extends SceneObject {

    private final Material material;
    private final SceneObject obj1;
    private final SceneObject obj2;

    public Subtraction(Material material, Cube cube, Sphere sphere) {
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
        float[] obj1Intercepts = obj1.getIntercepts(ray);
        float[] obj2Intercepts = obj2.getIntercepts(ray);

        if (!Float.isFinite(obj2Intercepts[0]) || obj1Intercepts[0] < obj2Intercepts[0]) {
            return obj1Intercepts[0];
        }
        if (obj2Intercepts[1] < obj1Intercepts[1]) {
            return obj2Intercepts[1];
        }
        return Float.NaN;

//        int count = 0;
//
//        int index1 = 0, index2=0;
//        while (count < 0 && index1 < obj1Intercepts.length && index2 < obj2Intercepts.length) {
//            if (obj1Intercepts[index1] < obj2Intercepts[index2]) {
//                return obj1Intercepts[0];
//            }
//        }

    }

    @Override
    public float[] getIntercepts(Ray ray) {
        return new float[0];
    }


    @Override
    public Vec getNormal(Ray ray, Vec interceptPoint) {
        float[] obj1Intercepts = obj1.getIntercepts(ray);
        float[] obj2Intercepts = obj2.getIntercepts(ray);

        if (!Float.isFinite(obj2Intercepts[0]) || obj1Intercepts[0] < obj2Intercepts[0]) {
            return obj1.getNormal(ray, interceptPoint);
        }
        if (obj2Intercepts[1] < obj1Intercepts[1]) {
            return obj2.getNormal(ray, interceptPoint).mul(-1);
        }
        return null;
    }

}
