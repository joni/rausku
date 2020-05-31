package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

public class DebugTexture implements Texture {
    @Override
    public Color getColor(Intercept intercept) {
        int code = intercept.info.hashCode();
        int r = code & 0xff;
        int g = (code >> 8) & 0xff;
        int b = (code >> 16) & 0xff;
        return Color.of(r / 255f, g / 255f, b / 255f);
    }
}
