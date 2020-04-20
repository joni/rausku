package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CSGIntersection implements CSGObject, SceneObject {

    private final Material material;

    private final CSGObject obj1;
    private final CSGObject obj2;

    public CSGIntersection(Material material, CSGObject object1, CSGObject object2) {
        this.material = material;
        this.obj1 = object1;
        this.obj2 = object2;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float[] intercepts1 = obj1.getAllIntercepts(ray);
        float[] intercepts2 = obj2.getAllIntercepts(ray);
        if (Float.isFinite(intercepts1[0]) && Float.isFinite(intercepts2[0])) {
            if (intercepts1[0] > intercepts2[0]) {
                if (intercepts1[0] > 0)
                    return new Intercept(intercepts1[0], ray.apply(intercepts1[0]), new IInfo(obj1, null));
            } else {
                if (intercepts2[0] > 0)
                    return new Intercept(intercepts2[0], ray.apply(intercepts2[0]), new IInfo(obj2, null));
            }
        }
//        Intercept intercept1 = obj1.getIntercept2(ray);
//        Intercept intercept2 = obj2.getIntercept2(ray);
//        if (intercept1.isValid() && intercept2.isValid()) {
//            if (intercept1.intercept > intercept2.intercept) {
//                return new Intercept(intercept1.intercept, intercept1.interceptPoint, new IInfo(obj1, intercept1));
//            } else {
//                return new Intercept(intercept1.intercept, intercept2.interceptPoint, new IInfo(obj2, intercept2));
//            }
//        }
        return Intercept.noIntercept();
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        float[] intercepts = obj1.getAllIntercepts(ray);
        float[] intercepts1 = obj2.getAllIntercepts(ray);
        float[] allIntercepts = {max(intercepts[0], intercepts1[0]), min(intercepts[1], intercepts1[1])};
        return allIntercepts;
    }

    @Override
    public Vec getNormal(Ray ray, Intercept interceptPoint) {
        IInfo intercept = (IInfo) interceptPoint.info;
        return intercept.object.getNormal(ray, interceptPoint);
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
