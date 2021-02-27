package rausku.lighting;

import rausku.geometry.Intercept;
import rausku.math.Ray;
import rausku.math.Vec;

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
    public Ray sampleRay(Intercept intercept) {
        return Ray.fromStartEnd(intercept.interceptPoint, position);
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
