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

        boolean inObj1 = false;
        boolean inObj2 = false;

        int obj1Index = 0;
        int obj2Index = 0;

        while (obj1Index < obj1Intercepts.length && obj2Index < obj2Intercepts.length) {
            if (obj1Intercepts[obj1Index] < obj2Intercepts[obj2Index]) {
                intercept = obj1Intercepts[obj1Index];
                // change in obj1. Are we entering or exiting?
                inObj1 = !inObj1;
                if (inObj1 && !inObj2 && intercept > 0) {
                    return new Intercept(intercept, ray.apply(intercept), obj1);
                }  // no change
                obj1Index++;
            } else {
                intercept = obj2Intercepts[obj2Index];
                // change in obj2. Are we entering or exiting?
                inObj2 = !inObj2;
                if (inObj1 && !inObj2 && intercept > 0) {
                    return new Intercept(intercept, ray.apply(intercept), obj2);
                }
                obj2Index++;
            }
        }

        while (obj1Index < obj1Intercepts.length) {
            intercept = obj1Intercepts[obj1Index];
            // change in obj1. Are we entering or exiting?
            inObj1 = !inObj1;
            if (inObj1 && !inObj2 && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj1);
            }  // no change
            obj1Index++;
        }

        while (obj2Index < obj2Intercepts.length) {
            intercept = obj2Intercepts[obj2Index];
            // change in obj2. Are we entering or exiting?
            inObj2 = !inObj2;
            if (inObj1 && !inObj2 && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj2);
            }
            obj2Index++;
        }

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
