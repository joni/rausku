package rausku.geometry;

import rausku.Material;
import rausku.Ray;
import rausku.math.Vec;

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
    public Intercept getIntercept2(Ray ray) {
        Intercept intercept1 = obj1.getIntercept2(ray);
        Intercept intercept2 = obj2.getIntercept2(ray);
        if (intercept1.isValid() && intercept2.isValid()) {
            if (intercept1.intercept > intercept2.intercept) {
                return new Intercept(intercept1.intercept, intercept1.interceptPoint, new IInfo(obj1, intercept1));
            } else {
                return new Intercept(intercept1.intercept, intercept2.interceptPoint, new IInfo(obj2, intercept2));
            }
        }
        return Intercept.noIntercept();
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        float[] intercepts = obj1.getIntercepts(ray);
        float[] intercepts1 = obj2.getIntercepts(ray);
        float[] allIntercepts = {max(intercepts[0], intercepts1[0]), min(intercepts[1], intercepts1[1])};
        return allIntercepts;
    }

    @Override
    public Vec getNormal(Ray ray, Intercept interceptPoint) {
        IInfo intercept = (IInfo) interceptPoint.info;
        return intercept.object.getNormal(ray, intercept.intercept);
    }

    private class IInfo {
        private final SceneObject object;
        private final Intercept intercept;

        public IInfo(SceneObject object, Intercept intercept) {
            this.object = object;
            this.intercept = intercept;
        }
    }
}
