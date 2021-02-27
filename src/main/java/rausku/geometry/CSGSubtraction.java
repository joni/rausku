package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public class CSGSubtraction implements CSGObject, SceneObject {

    private final CSGObject obj1;
    private final CSGObject obj2;

    public CSGSubtraction(CSGObject obj1, CSGObject obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
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
            if (inObj1 && !inObj2 && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj);
            }
        }

        while (obj1Index < obj1Intercepts.length) {
            float intercept = obj1Intercepts[obj1Index];
            // change in obj1. Are we entering or exiting?
            inObj1 = !inObj1;
            if (inObj1 && !inObj2 && intercept > 0) {
                return new Intercept(intercept, ray.apply(intercept), obj1);
            }  // no change
            obj1Index++;
        }

        while (obj2Index < obj2Intercepts.length) {
            float intercept = obj2Intercepts[obj2Index];
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
    public Vec getNormal(Intercept intercept) {
        IInfo info = (IInfo) intercept.info;
        // TODO the "intercept info" may have the wrong type
        if (info == obj1) {
            return info.object.getNormal(intercept);
        } else {
            return info.object.getNormal(intercept).mul(-1);
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return obj1.getBoundingBox();
    }

    static class IInfo {

        private CSGObject object;
        private Object objectInterceptInfo;

        public IInfo(CSGObject object, Object objectInterceptInfo) {
            this.object = object;
            this.objectInterceptInfo = objectInterceptInfo;
        }
    }
}
