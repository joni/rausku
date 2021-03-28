package rausku.lighting;

import rausku.geometry.Rectangle;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

import static rausku.math.FloatMath.abs;

public class RectangleLight implements LightSource {
    private final Matrix objectToWorld;
    private final Matrix worldToObject;
    private final Color color;
    private final Rectangle rectangle;

    public RectangleLight(Matrix objectToWorld, Color color) {
        this.objectToWorld = objectToWorld;
        this.worldToObject = objectToWorld.inverse();
        this.color = color;
        this.rectangle = new Rectangle();
    }

    @Override
    public int getSampleCount() {
        return 4;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        Vec sampledPoint = rectangle.sample(s, t);
        Vec worldLightPoint = objectToWorld.transform(sampledPoint);
        Ray ray = Ray.fromStartEnd(intercept.worldInterceptPoint, worldLightPoint);

        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint);
        var squaredDistance = toLight.sqLen();
        return new Sample(color, ray, squaredDistance * .25f / abs(ray.direction.y()));
    }

    @Override
    public boolean hasIntercept(Ray globalRay) {
        Ray localRay = worldToObject.transform(globalRay);
        return rectangle.hasIntercept(localRay);
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
