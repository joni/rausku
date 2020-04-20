package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

public class CSGSubtraction implements CSGObject, SceneObject {

    private final Material material;
    private final CSGObject obj1;
    private final CSGObject obj2;

    public CSGSubtraction(Material material, CSGObject obj1, CSGObject obj2) {
        this.material = material;
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float intercept = Float.NaN;
        float[] obj1Intercepts = obj1.getAllIntercepts(ray);
        float[] obj2Intercepts = obj2.getAllIntercepts(ray);

        if (!Float.isFinite(obj2Intercepts[0]) || obj1Intercepts[0] < obj2Intercepts[0]) {
            intercept = obj1Intercepts[0];
            return new Intercept(intercept, ray.apply(intercept), obj1);

        } else if (obj2Intercepts[1] < obj1Intercepts[1]) {
            intercept = obj2Intercepts[1];
            return new Intercept(intercept, ray.apply(intercept), obj2);
        }
        //        int count = 0;
        //
        //        int index1 = 0, index2=0;
        //        while (count < 0 && index1 < obj1Intercepts.length && index2 < obj2Intercepts.length) {
        //            if (obj1Intercepts[index1] < obj2Intercepts[index2]) {
        //                return obj1Intercepts[0];
        //            }
        //        }


        return Intercept.noIntercept();
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        return new float[0];
    }


    @Override
    public Vec getNormal(Ray ray, Intercept intercept) {
        CSGObject object = (CSGObject) intercept.info;
        // TODO the "intercept info" may have the wrong type
        if (object == obj1) {
            return object.getNormal(ray, intercept);
        } else {
            return object.getNormal(ray, intercept).mul(-1);
        }
    }

}
