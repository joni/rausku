package rausku.lighting;

import rausku.geometry.Geometry;
import rausku.geometry.SampleableGeometry;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

import static rausku.math.FloatMath.abs;

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
        return 4;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        Vec sampledPoint = geometry.sample(s, t);
        Vec worldLightPoint = objectToWorld.transform(sampledPoint);
        Ray ray = Ray.fromStartEnd(intercept.worldInterceptPoint, worldLightPoint);

        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint);
        var squaredDistance = toLight.sqLen();
        var likelihood = squaredDistance / (geometry.area() * abs(ray.direction.y()));
        return new Sample(radiance, ray, likelihood);
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
