package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

/**
 * Represents a single point as a light source.
 */
public class PointLight implements LightSource {
    private final Vec position;
    private final Color flux;

    /**
     * @param position
     * @param flux
     */
    public PointLight(Vec position, Color flux) {
        this.position = position;
        this.flux = flux;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        var squaredDistance = position.sub(intercept.worldInterceptPoint()).sqLen();
        var radiance = flux.div(squaredDistance);
        return new Sample(radiance, sampleRay(intercept, s, t), 1f);
    }

    private Ray sampleRay(SceneIntercept intercept, float s, float t) {
        return Ray.fromStartEnd(intercept.worldInterceptPoint(), position);
    }

    @Override
    public boolean hasIntercept(Ray ray) {
        return false;
    }

    @Override
    public Color evaluate() {
        return flux;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }
}
