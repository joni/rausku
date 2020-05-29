package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CSGUnion implements CSGObject, SceneObject {

    private CSGObject obj1;
    private CSGObject obj2;
    private BoundingBox bbox;

    public CSGUnion(CSGObject object1, CSGObject object2) {
        this.obj1 = object1;
        this.obj2 = object2;
        this.bbox = BoundingBox.union(obj1.getBoundingBox(), obj2.getBoundingBox());
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float[] obj1Intercepts = obj1.getAllIntercepts(ray);
        float[] obj2Intercepts = obj2.getAllIntercepts(ray);

        boolean inObj1 = false;
        boolean inObj2 = false;

        int obj1Index = 0;
        int obj2Index = 0;

        while (obj1Index < obj1Intercepts.length && obj2Index < obj2Intercepts.length) {
            float intercept;
            CSGObject obj;
            if (obj1Intercepts[obj1Index] < obj2Intercepts[obj2Index]) {
                // change in obj1
                intercept = obj1Intercepts[obj1Index];
                obj = obj1;
                inObj1 = !inObj1;
                obj1Index++;
            } else {
                // change in obj2
                intercept = obj2Intercepts[obj2Index];
                obj = obj2;
                inObj2 = !inObj2;
                obj2Index++;
            }
            // Are we entering or exiting?
            if ((inObj1 || inObj2) && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj);
            }
        }

        while (obj1Index < obj1Intercepts.length) {
            float intercept = obj1Intercepts[obj1Index];
            // change in obj1. Are we entering or exiting?
            inObj1 = !inObj1;
            if ((inObj1 || inObj2) && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj1);
            }  // no change
            obj1Index++;
        }

        while (obj2Index < obj2Intercepts.length) {
            float intercept = obj2Intercepts[obj2Index];
            // change in obj2. Are we entering or exiting?
            inObj2 = !inObj2;
            if ((inObj1 || inObj2) && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj2);
            }
            obj2Index++;
        }

        return Intercept.noIntercept();
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        float[] intercepts1 = obj1.getAllIntercepts(ray);
        float[] intercepts2 = obj2.getAllIntercepts(ray);
        float[] allIntercepts = {max(intercepts1[0], intercepts2[0]), min(intercepts1[1], intercepts2[1])};
        return allIntercepts;
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        CSGObject object = (CSGObject) intercept.info;
        return object.getNormal(intercept);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bbox;
    }

    private static class IInfo {
        private final CSGObject object;
        private final Intercept intercept;

        public IInfo(CSGObject object, Intercept intercept) {
            this.object = object;
            this.intercept = intercept;
        }
    }
}