package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.Arrays;
import java.util.Comparator;

import static java.lang.Math.abs;

public class Cube implements CSGObject, SceneObject {

    private static final double BOUNDS = 1.000001;
    private BoundingBox bbox = new BoundingBox(Vec.point(-1, -1, -1), Vec.point(1, 1, 1));

    @Override
    public Vec getNormal(Intercept intercept) {
        IInfo info = (IInfo) intercept.info;
        return switch (info.face) {
            case 0 -> Vec.of(+1, 0, 0);
            case 1 -> Vec.of(-1, 0, 0);
            case 2 -> Vec.of(0, +1, 0);
            case 3 -> Vec.of(0, -1, 0);
            case 4 -> Vec.of(0, 0, +1);
            case 5 -> Vec.of(0, 0, -1);
            default -> throw new IllegalStateException("Unexpected value: " + info.face);
        };
    }

    @Override
    public Intercept[] getAllInterceptObjects(Ray ray) {
        float[] intercepts = {
                (+1 - ray.origin.x) / ray.direction.x,
                (-1 - ray.origin.x) / ray.direction.x,
                (+1 - ray.origin.y) / ray.direction.y,
                (-1 - ray.origin.y) / ray.direction.y,
                (+1 - ray.origin.z) / ray.direction.z,
                (-1 - ray.origin.z) / ray.direction.z,
        };

        Intercept[] interceptObjs = new Intercept[2];
        int index = 0;

        for (int i = 0; i < intercepts.length; i++) {
            float intercept1 = intercepts[i];
            if (isOk(ray, intercept1)) {
                interceptObjs[index++] = new Intercept(intercept1, ray.apply(intercept1), new IInfo(i));
            }
        }
        if (index < 2) {
            return new Intercept[0];
        }

        Arrays.sort(interceptObjs, Comparator.comparing(i -> i.intercept));

        return interceptObjs;
    }

    public Intercept getIntercept(Ray ray) {
        float[] intercepts = {
                (+1 - ray.origin.x) / ray.direction.x,
                (-1 - ray.origin.x) / ray.direction.x,
                (+1 - ray.origin.y) / ray.direction.y,
                (-1 - ray.origin.y) / ray.direction.y,
                (+1 - ray.origin.z) / ray.direction.z,
                (-1 - ray.origin.z) / ray.direction.z,
        };
        int face = -1;
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (int i = 0; i < intercepts.length; i++) {
            float intercept1 = intercepts[i];
            if (isOk(ray, intercept1) && intercept1 < closestIntercept && intercept1 > SceneObject.INTERCEPT_NEAR) {
                closestIntercept = intercept1;
                face = i;
            }
        }
        if (!Float.isFinite(closestIntercept)) {
            return Intercept.noIntercept();
        }

        float intercept = closestIntercept;
        IInfo info = new IInfo(face);
        return new Intercept(intercept, ray.apply(intercept), info);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bbox;
    }

    private boolean isOk(Ray ray, float intercept) {
        Vec interceptPoint = ray.apply(intercept);
        return abs(interceptPoint.x) < BOUNDS && abs(interceptPoint.y) < BOUNDS && abs(interceptPoint.z) < BOUNDS;
    }

    static class IInfo {

        private int face;

        public IInfo(int face) {
            this.face = face;
        }
    }
}
