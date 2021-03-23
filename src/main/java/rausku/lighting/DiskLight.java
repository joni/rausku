package rausku.lighting;

import rausku.geometry.BoundingBox;
import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.math.*;
import rausku.scenes.SceneIntercept;

public class DiskLight implements LightSource, SceneObject {
    private final Matrix transform;
    private final Matrix inverse;
    private final Color color;

    public DiskLight(Matrix transform, Color color) {
        this.transform = transform;
        this.inverse = transform.inverse();
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        Vec sampledPoint = Rand.uniformDisk(s, t).toPoint();
        Vec worldLightPoint = transform.transform(sampledPoint);
        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint);
        var squaredDistance = toLight.sqLen();
        Ray ray = Ray.fromStartEnd(intercept.worldInterceptPoint, worldLightPoint);
        return new Sample(color, ray, squaredDistance / (FloatMath.PI * ray.direction.y));
    }

    @Override
    public boolean intercepts(Ray globalRay) {
        Ray localRay = inverse.transform(globalRay);
        float intercept = -localRay.origin.y / localRay.direction.y;
        Vec interceptPoint = localRay.apply(intercept);
        return interceptPoint.sqLen() < 2;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        return Vec.J.mul(-1);
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float intercept = -ray.origin.y / ray.direction.y;
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
}
