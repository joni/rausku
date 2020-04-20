package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;

public class DirectionalLight {
    private Vec towardsLight;
    private Color color;

    public DirectionalLight(Vec direction, Color color) {
        this.towardsLight = direction.normalize().mul(-1);
        this.color = color;
    }

    public Ray getRay(Vec origin) {
        return new Ray(origin, towardsLight);
    }

    public Vec getDirection() {
        return towardsLight;
    }

    public Color getColor() {
        return color;
    }

    public boolean intercepts(Ray ray) {
        return Vec.cos(getDirection(), ray.getDirection()) > .99;
    }
}
