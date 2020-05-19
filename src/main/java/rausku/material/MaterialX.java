package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

import static rausku.math.FloatMath.*;


public class MaterialX extends Material {

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {

        float baseValue = 4 * sqrt(pow(sin(interceptPoint.x * 10) + 1, 2) + pow(sin(interceptPoint.y * 10) + 1, 2) + pow(sin(interceptPoint.z * 10) + 1, 2));
        float x = 1 / (1 + exp(-sin(baseValue * PI / 2)));

        return Color.of(x, x, x);
    }
}
