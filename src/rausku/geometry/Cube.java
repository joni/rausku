package rausku.geometry;

import rausku.Material;
import rausku.Ray;
import rausku.Scene;
import rausku.math.Vec;

import java.util.Arrays;

import static java.lang.Math.abs;

public class Cube extends SceneObject {

    private static final double BOUNDS = 1.000001;
    private final Material material;

    public Cube(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
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
    public float[] getIntercepts(Ray ray) {
        Vec rayOrigin = ray.getOrigin();
        Vec rayDirection = ray.getDirection();
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

    @Override
    public float getIntercept(Ray ray) {
        ray.getOrigin();
        float[] intercepts = {
                (+1 - ray.getOrigin().x) / ray.getDirection().x,
                (-1 - ray.getOrigin().x) / ray.getDirection().x,
                (+1 - ray.getOrigin().y) / ray.getDirection().y,
                (-1 - ray.getOrigin().y) / ray.getDirection().y,
                (+1 - ray.getOrigin().z) / ray.getDirection().z,
                (-1 - ray.getOrigin().z) / ray.getDirection().z,
        };
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (float intercept : intercepts) {
            if (isOk(ray, intercept) && intercept < closestIntercept) {
                closestIntercept = intercept;
            }
        }
        if (Float.isFinite(closestIntercept)) {
            return closestIntercept;
        } else {
            return Float.NaN;
        }
    }

    private boolean isOk(Ray ray, float intercept) {
        if (intercept < Scene.INTERCEPT_NEAR) {
            return false;
        }
        Vec interceptPoint = ray.apply(intercept);
        return abs(interceptPoint.x) < BOUNDS && abs(interceptPoint.y) < BOUNDS && abs(interceptPoint.z) < BOUNDS;
    }
}
