package rausku.lighting;

import rausku.math.FloatMath;
import rausku.math.Rand;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

/**
 * Represents a light source that surrounds the scene and has a constant emission in all directions.
 */
public class AmbientLight implements LightSource {
    private Color color;

    public AmbientLight(Color color) {
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        // TODO could be better - we could pick the hemisphere
        Vec direction = Rand.sphere(s, t);
        var rayToLight = Ray.fromOriginDirection(intercept.worldInterceptPoint(), direction);
        return new Sample(color, rayToLight, 1 / (4 * FloatMath.PI));
    }

    @Override
    public boolean hasIntercept(Ray ray) {
        return true;
    }

    public Color evaluate() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }
}
