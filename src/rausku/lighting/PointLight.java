package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;

public class PointLight implements LightSource {
    private final Vec position;
    private final Color color;

    public PointLight(Vec position, Color color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public Ray getRay(Vec origin) {
        return Ray.fromStartEnd(origin, position);
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
    public float getMaxIntercept(Ray lightRay) {
        return position.sub(lightRay.getOrigin()).len();
    }
}
