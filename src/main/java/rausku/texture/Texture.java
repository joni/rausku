package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

public interface Texture {
    Color getColor(Intercept intercept);
}
