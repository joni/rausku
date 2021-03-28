package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

import static rausku.math.FloatMath.cos;

public class ConeLight implements LightSource {
    private final Vec position;
    private final Vec direction;
    private final float cosAngle;
    private final float cosAngle2;
    private final Color color;

    public ConeLight(Vec position, Vec direction, float angle, Color color) {
        this(position, direction, angle, angle, color);
    }

    public ConeLight(Vec position, Vec direction, float innerAngle, float outerAngle, Color color) {
        this.position = position;
        this.direction = direction;
        this.cosAngle = cos(innerAngle / 2);
        this.cosAngle2 = cos(outerAngle / 2);
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        // TODO compute likelihood
        return new Sample(color.mul(getIntensity(intercept.worldInterceptPoint)), sampleRay(intercept, s, t), 1);
    }

    private Ray sampleRay(SceneIntercept intercept, float s, float t) {
        // TODO actually sample it
        return Ray.fromStartEnd(intercept.worldInterceptPoint, position);
    }

    @Override
    public boolean hasIntercept(Ray ray) {
        // TODO compute ray-point distance
        return false;
    }

    @Override
    public Color evaluate() {
        return color;
    }

    @Override
    public float getIntensity(Vec origin) {
        Vec lightToObject = origin.sub(position);
        float cos = Vec.cos(lightToObject, direction);
        if (cos > cosAngle) {
            return 1;
        } else if (cos > cosAngle2) {
            return (cos - cosAngle2) / (cosAngle - cosAngle2);
        } else {
            return 0;
        }
    }
}
