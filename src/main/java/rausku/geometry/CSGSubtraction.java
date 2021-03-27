package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public class CSGSubtraction implements CSGObject, Geometry {

    private final CSGObject obj1;
    private final CSGObject obj2;

    public CSGSubtraction(CSGObject obj1, CSGObject obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        Intercept[] obj1Intercepts = obj1.getAllInterceptObjects(ray);
        Intercept[] obj2Intercepts = obj2.getAllInterceptObjects(ray);

        boolean inObj1 = false;
        boolean inObj2 = false;

        int obj1Index = 0;
        int obj2Index = 0;

        while (obj1Index < obj1Intercepts.length && obj2Index < obj2Intercepts.length) {
            Intercept intercept;
            CSGObject obj;
            if (obj1Intercepts[obj1Index].intercept < obj2Intercepts[obj2Index].intercept) {
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
            if (inObj1 && !inObj2 && intercept.intercept > 0) {
                return createIntercept(ray, intercept, obj);
            }
        }

        while (obj1Index < obj1Intercepts.length) {
            Intercept icept = obj1Intercepts[obj1Index];
            // change in obj1. Are we entering or exiting?
            inObj1 = !inObj1;
            if (inObj1 && !inObj2 && icept.intercept > 0) {
                return createIntercept(ray, icept, obj1);
            }  // no change
            obj1Index++;
        }

        while (obj2Index < obj2Intercepts.length) {
            Intercept icept = obj2Intercepts[obj2Index];
            // change in obj2. Are we entering or exiting?
            inObj2 = !inObj2;
            if (inObj1 && !inObj2 && icept.intercept > 0) {
                return createIntercept(ray, icept, obj2);
            }
            obj2Index++;
        }

        return Intercept.noIntercept();
    }

    private Intercept createIntercept(Ray ray, Intercept intercept, CSGObject obj) {
        IInfo iinfo = new IInfo(obj, intercept);
        return new Intercept(intercept, iinfo);
    }

    @Override
    public Intercept[] getAllInterceptObjects(Ray ray) {
        return new Intercept[0];
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        IInfo info = (IInfo) intercept.info;
        // TODO the "intercept info" may have the wrong type
        if (info.object == obj1) {
            return info.object.getNormal(info.intercept);
        } else {
            return info.object.getNormal(info.intercept).mul(-1);
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return obj1.getBoundingBox();
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
