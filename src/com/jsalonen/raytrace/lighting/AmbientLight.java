package com.jsalonen.raytrace.lighting;

import com.jsalonen.raytrace.Color;

public class AmbientLight {
    private Color color;

    public AmbientLight(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
