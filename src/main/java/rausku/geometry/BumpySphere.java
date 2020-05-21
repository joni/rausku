package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.sin;

public class BumpySphere extends Sphere {
    public BumpySphere(Vec center, float radius) {
        super(center, radius);
    }

    @Override
    public Vec getNormal(Ray ray, Intercept intercept) {
        Vec point = intercept.interceptPoint;
        Vec bump = Vec.of(sin(point.x * 50), sin(point.y * 50), sin(point.z * 50)).div(50);
        return super.getNormal(ray, intercept).add(bump);
    }
}
