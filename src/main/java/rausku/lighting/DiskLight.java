package rausku.lighting;

import rausku.math.*;
import rausku.scenes.SceneIntercept;

public class DiskLight implements LightSource {
    private final Matrix transform;
    private final Matrix inverse;
    private final Color color;

    public DiskLight(Matrix transform, Color color) {
        this.transform = transform;
        this.inverse = transform.inverse();
        this.color = color;
    }

    @Override
    public int getSampleCount() {
        return 4;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        Vec sampledPoint = Rand.uniformDisk(s, t).toPoint();
        Vec worldLightPoint = transform.transform(sampledPoint);
        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint);
        var squaredDistance = toLight.sqLen();
        Ray ray = Ray.fromStartEnd(intercept.worldInterceptPoint, worldLightPoint);
        return new Sample(color, ray, squaredDistance / (FloatMath.PI * ray.direction.y()));
    }

    @Override
    public boolean hasIntercept(Ray globalRay) {
        Ray localRay = inverse.transform(globalRay);
        float intercept = -localRay.origin.y() / localRay.direction.y();
        Vec interceptPoint = localRay.apply(intercept);
        return interceptPoint.sqLen() < 2;
    }

    @Override
    public Color evaluate() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }

}
