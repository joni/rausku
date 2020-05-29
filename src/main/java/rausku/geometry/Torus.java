package rausku.geometry;

import rausku.math.FloatMath;
import rausku.math.Ray;
import rausku.math.Vec;

public class Torus implements SceneObject {

    public static final float TOLERANCE = 1e-6f;
    private final float R;
    private final float r;
    private BoundingBox bbox;

    public Torus(float R, float r) {
        this.R = R;
        this.r = r;
        this.bbox = new BoundingBox(
                -R - r, R + r,
                -r, +r,
                -R - r, R + r
        );
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        Vec pointOnRay = ((Vec) intercept.info);
        Vec pointOnCircle = Vec.of(pointOnRay.x, 0, pointOnRay.z).normalize().mul(R);
        return pointOnRay.sub(pointOnCircle).normalize();
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        if (!bbox.testIntercept(ray)) {
            return Intercept.noIntercept();
        }
        float[] bboxIntercepts = bbox.getIntercepts(ray);
        float t = 0;

        float len = ray.direction.len();

        while (t < bboxIntercepts[1]) {
            Vec pointOnRay = ray.apply(t);

            float distanceToTorus = signedTorusDistance(pointOnRay);

            if (distanceToTorus < TOLERANCE) {
                return new Intercept(t, pointOnRay, pointOnRay);
            } else {
                t = t + distanceToTorus / len;
            }
        }
        return Intercept.noIntercept();
    }

    private float signedTorusDistance(Vec pointOnRay) {
        if (pointOnRay.x == 0 && pointOnRay.z == 0) {
            return FloatMath.hypot(R, pointOnRay.y) - r;
        }
        Vec pointOnCircle = Vec.of(pointOnRay.x, 0, pointOnRay.z).normalize().mul(R);
        float distanceToCircle = pointOnCircle.sub(pointOnRay).len();
        return distanceToCircle - r;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bbox;
    }
}
