package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.PI;

public class Disk implements Geometry, SampleableGeometry {

    @Override
    public Vec getNormal(Intercept intercept) {
        return Vec.J.mul(-1);
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float intercept = -ray.origin.y() / ray.direction.y();
        Vec interceptPoint = ray.apply(intercept);
        if (interceptPoint.sqLen() < 2) {
            return new Intercept(intercept, interceptPoint, null);
        } else {
            return Intercept.noIntercept();
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(-1, 1, 0, 0, -1, 1);
    }

    @Override
    public Vec sample(float s, float t) {
        return null;
    }

    @Override
    public boolean hasIntercept(Ray ray) {
        return false;
    }

    @Override
    public float area() {
        return PI;
    }
}
