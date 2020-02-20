package com.jsalonen.raytrace.lighting;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.math.Vec;

public class DirectionalLight {
    private Vec towardsLight;
    private Color color;

    public DirectionalLight(Vec direction, Color color) {
        this.towardsLight = direction.mul(-1);
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
}
