package rausku.lighting;

import rausku.math.FloatMath;
import rausku.math.Rand;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

public class AmbientLight implements LightSource {
    private Color color;

    public AmbientLight(Color color) {
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        return new Sample(color, sampleRay(intercept, s, t), 1 / (2 * FloatMath.PI));
    }

    private Ray sampleRay(SceneIntercept intercept, float s, float t) {
        Vec hemisphere = Rand.hemisphere(s, t);
        return Ray.fromOriginDirection(intercept.worldInterceptPoint, hemisphere);
    }

    @Override
    public boolean intercepts(Ray ray) {
        return true;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }
}
