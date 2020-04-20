package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CSGUnion implements CSGObject, SceneObject {

    private final Material material;
    private CSGObject obj1;
    private CSGObject obj2;

    public CSGUnion(Material material, CSGObject object1, CSGObject object2) {
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
        if (Float.isFinite(intercepts1[0]) || Float.isFinite(intercepts2[0])) {
            if (intercepts1[0] > intercepts2[0]) {
                if (intercepts2[0] > 0)
                    return new Intercept(intercepts2[0], ray.apply(intercepts2[0]), new IInfo(obj2, null));
            } else if (intercepts2[0] > intercepts1[0]) {
                if (intercepts1[0] > 0) {
                    return new Intercept(intercepts1[0], ray.apply(intercepts1[0]), new IInfo(obj1, null));
                }
            }
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
    public Vec getNormal(Ray ray, Intercept interceptPoint) {
        IInfo intercept = (IInfo) interceptPoint.info;
        return intercept.object.getNormal(ray, intercept.intercept);
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
