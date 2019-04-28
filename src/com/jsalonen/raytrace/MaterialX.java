package com.jsalonen.raytrace;

import com.jsalonen.raytrace.geometry.Vec;

import static java.lang.Math.*;

public class MaterialX extends Material {
    public MaterialX(Color diffuseColor) {
        super(diffuseColor);
    }

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {

        double baseValue = 4 * sqrt(pow(sin(interceptPoint.x * 10) + 1, 2) + pow(sin(interceptPoint.y * 10) + 1, 2) + pow(sin(interceptPoint.z * 10) + 1, 2));
        double x = 1 / (1 + Math.exp(-Math.sin(baseValue * Math.PI / 2)));

        return Color.of(x, x, x);
    }
}
