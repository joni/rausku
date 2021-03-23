package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

public class PointLight implements LightSource {
    private final Vec position;
    private final Color color;

    public PointLight(Vec position, Color color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        // TODO is the likelihood correct?
        var squaredDistance = position.sub(intercept.worldInterceptPoint).sqLen();
        return new Sample(color, sampleRay(intercept, s, t), squaredDistance);
    }

    private Ray sampleRay(SceneIntercept intercept, float s, float t) {
        return Ray.fromStartEnd(intercept.worldInterceptPoint, position);
    }

    @Override
    public boolean intercepts(Ray ray) {
        // TODO compute ray-point distance
        return false;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }
}
