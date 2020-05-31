package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

import static rausku.math.FloatMath.floor;

public class GradientTexture implements Texture {

    float size = 1;
    Color uColor = Color.of(1, 0, 0);
    Color vColor = Color.of(0, 1, 0);

    @Override
    public Color getColor(Intercept intercept) {
        float u1 = intercept.u / size;
        float v1 = intercept.v / size;
        float u = u1 - floor(u1);
        float v = v1 - floor(v1);
        return uColor.mulAdd(u, vColor.mul(v));
    }
}
