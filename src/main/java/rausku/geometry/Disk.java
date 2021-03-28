package rausku.geometry;

import rausku.math.Rand;
import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.PI;

public class Disk implements Geometry, SampleableGeometry {

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(-1, 1, 0, 0, -1, 1);
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        return Vec.J;
    }

    @Override
    public Sample sample(float s, float t) {
        return new Sample(Rand.uniformDisk(s, t).toPoint(), Vec.J, 1 / PI);
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
    public boolean hasIntercept(Ray ray) {
        float intercept = -ray.origin.y() / ray.direction.y();
        if (intercept > ray.bound) {
            return false;
        }
        Vec interceptPoint = ray.apply(intercept);
        return interceptPoint.sqLen() < 2;
    }

}
