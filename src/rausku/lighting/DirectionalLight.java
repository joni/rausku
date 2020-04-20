package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;

public class DirectionalLight implements LightSource {
    private Vec towardsLight;
    private Color color;

    public DirectionalLight(Vec direction, Color color) {
        this.towardsLight = direction.normalize().mul(-1);
        this.color = color;
    }

    @Override
    public Ray getRay(Vec origin) {
        return Ray.fromOriginDirection(origin, towardsLight);
    }

    public Vec getDirection() {
        return towardsLight;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }

    @Override
    public boolean intercepts(Ray ray) {
        return Vec.cos(getDirection(), ray.getDirection()) > .99;
    }
}
