package rausku.lighting;

import rausku.geometry.Geometry;
import rausku.geometry.SampleableGeometry;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

import static java.lang.Math.max;

public class AreaLight implements LightSource {

    private final Matrix objectToWorld;
    private final Matrix worldToObject;
    private final Color radiance;
    private final SampleableGeometry geometry;

    public AreaLight(Matrix objectToWorld, Color radiance, SampleableGeometry geometry) {
        this.objectToWorld = objectToWorld;
        this.worldToObject = objectToWorld.inverse();
        this.radiance = radiance;
        this.geometry = geometry;
    }

    @Override
    public int getSampleCount() {
        return 16;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        var sample = geometry.sample(s, t);
        Vec worldLightPoint = objectToWorld.transform(sample.point());
        var worldNormal = worldToObject.transposeTransform(sample.normal());
        Ray rayToLight = Ray.fromStartEnd(intercept.worldInterceptPoint(), worldLightPoint);

        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint());
        var squaredDistance = toLight.sqLen();
        var cosine = -Vec.dot(rayToLight.direction, worldNormal);
        if (cosine < 0) {
            return new Sample(Color.black(), rayToLight, Float.POSITIVE_INFINITY);
        }
        var likelihood = sample.likelihood() * squaredDistance / max(0, cosine);
        return new Sample(radiance, rayToLight, likelihood);
    }

    @Override
    public boolean hasIntercept(Ray ray) {
        var localRay = worldToObject.transform(ray);
        return geometry.hasIntercept(localRay);
    }

    @Override
    public Color evaluate() {
        return radiance;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1f;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public Matrix getObjectToWorld() {
        return objectToWorld;
    }

    public Matrix getWorldToObject() {
        return worldToObject;
    }
}
