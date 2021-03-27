package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public class CSGIntersection implements CSGObject, Geometry {

    private final CSGObject obj1;
    private final CSGObject obj2;
    private BoundingBox bbox;

    public CSGIntersection(CSGObject object1, CSGObject object2) {
        this.obj1 = object1;
        this.obj2 = object2;
        this.bbox = BoundingBox.intersection(obj1.getBoundingBox(), obj2.getBoundingBox());
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
            if (inObj1 && inObj2 && intercept.intercept > 0) {
                return new Intercept(intercept, new IInfo(obj, intercept));
            }
        }

        while (obj1Index < obj1Intercepts.length) {
            Intercept intercept = obj1Intercepts[obj1Index];
            // change in obj1. Are we entering or exiting?
            inObj1 = !inObj1;
            if (inObj1 && inObj2 && intercept.intercept > 0) {
                return new Intercept(intercept, new IInfo(obj1, intercept));
            }  // no change
            obj1Index++;
        }

        while (obj2Index < obj2Intercepts.length) {
            Intercept intercept = obj2Intercepts[obj2Index];
            // change in obj2. Are we entering or exiting?
            inObj2 = !inObj2;
            if (inObj1 && inObj2 && intercept.intercept > 0) {
                return new Intercept(intercept, new IInfo(obj2, intercept));
            }
            obj2Index++;
        }

        return Intercept.noIntercept();
    }

    @Override
    public Intercept[] getAllInterceptObjects(Ray ray) {
        return new Intercept[0];
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bbox;
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        IInfo info = (IInfo) intercept.info;
        return info.object.getNormal(info.intercept);
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
