package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

import static rausku.math.FloatMath.*;

public class TextureX implements Texture {

    @Override
    public Color getColor(Intercept intercept) {

        float baseValue = 4 * sqrt(pow(sin(intercept.interceptPoint.x * 10) + 1, 2) + pow(sin(intercept.interceptPoint.y * 10) + 1, 2) + pow(sin(intercept.interceptPoint.z * 10) + 1, 2));
        float x = 1 / (1 + exp(-sin(baseValue * PI / 2)));

        return Color.of(x, x, x);
    }
}
