package rausku.geometry;

import rausku.Material;
import rausku.Ray;
import rausku.math.Vec;

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
    public Intercept getIntercept2(Ray ray) {
        float intercept = Float.NaN;
        float[] obj1Intercepts = obj1.getIntercepts(ray);
        float[] obj2Intercepts = obj2.getIntercepts(ray);

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
    public Vec getNormal(Ray ray, Intercept intercept) {
        SceneObject object = (SceneObject) intercept.info;
        // TODO the "intercept info" may have the wrong type
        return object.getNormal(ray, intercept);
    }

}
