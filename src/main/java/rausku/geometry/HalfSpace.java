package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public class HalfSpace implements CSGObject, SceneObject {
    private final Vec normal;

    Vec u, v;

    public HalfSpace(Vec normal) {
        this.normal = normal;
        this.u = normal.perpendicular();
        this.v = Vec.cross(u, normal);
    }

    public static HalfSpace horizontalPlane(float groundLevel) {
        return new HalfSpace(Vec.of(0, 1, 0, -groundLevel));
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        // v.(dt+o) > 0  <=> (v.d)t > -v.o  <=> t <> -v.o/v.d
        float vDotD = Vec.dot(normal, ray.direction);
        float intercept = -Vec.dot(normal, ray.origin) / vDotD;
        if (vDotD > 0) {
            return new float[]{Float.NEGATIVE_INFINITY, intercept};
        } else {
            return new float[]{intercept, Float.POSITIVE_INFINITY};
        }
    }

    public Intercept getIntercept(Ray ray) {
        float intercept = getIntercept0(ray);
        Vec interceptPt = ray.apply(intercept);
        return new Intercept(intercept, interceptPt, u.dot(interceptPt), v.dot(interceptPt), null);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return BoundingBox.unbounded();
    }

    private float getIntercept0(Ray ray) {
        // v.(dt+o) = 0  <=> (v.d)t = - v.o  <=> t = -v.o/v.d
        float intercept = -Vec.dot(normal, ray.origin) / Vec.dot(normal, ray.direction);
        if (intercept > SceneObject.INTERCEPT_NEAR) {
            return intercept;
        } else {
            return Float.NaN;
        }
    }

    public Vec getNormal(Intercept point) {
        return normal.toVector();
    }

    @Override
    public String toString() {
        return String.format("HalfSpace%s", normal);
    }
}
