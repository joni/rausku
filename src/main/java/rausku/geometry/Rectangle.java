package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

/**
 * One-sided rectangle
 */
public class Rectangle implements Geometry, SampleableGeometry {

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(-1, 1, 0, 0, -1, 1);
    }

    /**
     * Sample a point in the rectangle
     *
     * @param s
     * @param t
     * @return point on the rectangle
     */
    public Vec sample(float s, float t) {
        return Vec.point(2 * s - 1, 0, 2 * t - 1);
    }

    public boolean hasIntercept(Ray ray) {
        float intercept = -ray.origin.y() / ray.direction.y();
        Vec interceptPoint = ray.apply(intercept);
        return -1 <= interceptPoint.x() && interceptPoint.x() <= 1 &&
                -1 <= interceptPoint.z() && interceptPoint.z() <= 1;
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float intercept = -ray.origin.y() / ray.direction.y();
        Vec interceptPoint = ray.apply(intercept);

        if (-1 <= interceptPoint.x() && interceptPoint.x() <= 1 &&
                -1 <= interceptPoint.z() && interceptPoint.z() <= 1) {
            return new Intercept(intercept, interceptPoint, null);
        } else {
            return Intercept.noIntercept();
        }
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        return Vec.J.mul(-1);
    }

    @Override
    public float area() {
        return 4;
    }
}
