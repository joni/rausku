package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.Arrays;

import static java.lang.Math.abs;

public class Cube implements CSGObject, SceneObject {

    private static final double BOUNDS = 1.000001;

    public Cube() {
    }

    @Override
    public Vec getNormal(Ray ray, Intercept intercept) {
        Vec interceptPoint = intercept.interceptPoint;
        float absx = abs(interceptPoint.x);
        float absy = abs(interceptPoint.y);
        float absz = abs(interceptPoint.z);
        if (absx > absy && absx > absz) {
            return Vec.of(interceptPoint.x, 0, 0).normalize();
        }
        if (absy > absx && absy > absz) {
            return Vec.of(0, interceptPoint.y, 0).normalize();
        }
        if (absz > absx && absz > absy) {
            return Vec.of(0, 0, interceptPoint.z).normalize();
        }
        return interceptPoint.normalize();
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        Vec rayOrigin = ray.origin;
        Vec rayDirection = ray.direction;
        float[] intercepts = {Float.NaN, Float.NaN};
        int index = 0;
        float[] possibleIntercepts = {
                (+1 - rayOrigin.x) / rayDirection.x,
                (-1 - rayOrigin.x) / rayDirection.x,
                (+1 - rayOrigin.y) / rayDirection.y,
                (-1 - rayOrigin.y) / rayDirection.y,
                (+1 - rayOrigin.z) / rayDirection.z,
                (-1 - rayOrigin.z) / rayDirection.z,
        };
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (float intercept : possibleIntercepts) {
            if (isOk(ray, intercept) && index < 2) {
                closestIntercept = intercept;
                intercepts[index++] = intercept;
            }
        }
        Arrays.sort(intercepts);
        return intercepts;
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
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (float intercept1 : intercepts) {
            if (isOk(ray, intercept1) && intercept1 < closestIntercept && intercept1 > SceneObject.INTERCEPT_NEAR) {
                closestIntercept = intercept1;
            }
        }
        float intercept;
        if (Float.isFinite(closestIntercept)) {
            intercept = closestIntercept;
        } else {
            intercept = Float.NaN;
        }
        return new Intercept(intercept, ray.apply(intercept), null);
    }

    private boolean isOk(Ray ray, float intercept) {
        Vec interceptPoint = ray.apply(intercept);
        return abs(interceptPoint.x) < BOUNDS && abs(interceptPoint.y) < BOUNDS && abs(interceptPoint.z) < BOUNDS;
    }
}
