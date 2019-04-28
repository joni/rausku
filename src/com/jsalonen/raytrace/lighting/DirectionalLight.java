package com.jsalonen.raytrace.lighting;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.geometry.Vec;

public class DirectionalLight {
    private Vec direction;
    private Color color;

    public DirectionalLight(Vec direction, Color color) {
        this.direction = direction;
        this.color = color;
    }

    public Ray getRay(Vec origin) {
        return new Ray(origin, direction.copy().mul(-1));
    }

    public Vec getDirection() {
        return direction;
    }

    public Color getColor() {
        return color;
    }
}
