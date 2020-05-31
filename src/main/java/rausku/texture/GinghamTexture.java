package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

import static rausku.math.FloatMath.ceil;

public class GinghamTexture implements Texture {
    private final float scale;
    private final Color[] diffuseColor;

    public GinghamTexture(float scale, Color color) {
        this(scale, color, color);
    }

    public GinghamTexture(float scale, Color color1, Color color2) {
        this(scale, Color.of(1, 1, 1), color1, color2, color1.mul(color2));
    }

    public GinghamTexture(float scale, Color color1, Color color2, Color color3) {
        this(scale, Color.of(1, 1, 1), color1, color2, color3);
    }

    public GinghamTexture(float scale, Color color1, Color color2, Color color3, Color color4) {
        this.scale = scale;
        this.diffuseColor = new Color[]{color1, color2, color3, color4};
    }

    @Override
    public Color getColor(Intercept intercept) {
        int baseValue = ((ceil(intercept.u / scale) & 1) << 1) | (ceil(intercept.v / scale) & 1);
        return diffuseColor[baseValue];
    }
}
