package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class Metaballs implements SceneObject {

    private static final float TOLERANCE = 1e-3f;

    final List<Vec> points;
    final BoundingBox bbox;

    public Metaballs(List<Vec> points) {
        this.points = points;
        float r = 5;
        this.bbox = new BoundingBox(-r, r, -r, r, -r, r);
    }


    private float signedDistance(Vec pointOnRay) {
        float d = 0;
        for (Vec point : points) {
            d += 1 / Vec.mulAdd(1, pointOnRay, -1, point).sqLen();
        }
        return 1f - d / points.size();
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        Vec pointOnRay = ((Vec) intercept.info);

        Vec gradient = Vec.of(0, 0, 0);
        for (Vec point : points) {
            Vec fromCenter = Vec.mulAdd(1, pointOnRay, -1, point);
            float sqLen = fromCenter.sqLen();
            Vec part = fromCenter.mul(2 / (sqLen * sqLen));
            gradient = gradient.add(part);
        }
        return gradient.normalize();
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        if (!bbox.testIntercept(ray)) {
            return Intercept.noIntercept();
        }
        float[] bboxIntercepts = bbox.getIntercepts(ray);
        float t = bboxIntercepts[0];

        float len = ray.direction.len();

        while (t < bboxIntercepts[1]) {
            Vec pointOnRay = ray.apply(t);

            float signedDistance = signedDistance(pointOnRay);

            if (signedDistance < TOLERANCE) {
                return new Intercept(t, pointOnRay, pointOnRay);
            } else {
                t = t + signedDistance / len;
            }
        }
        return Intercept.noIntercept();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return null;
    }
}
