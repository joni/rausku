package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Noise;
import rausku.math.PerlinNoise2D;

public class NoiseTexture implements Texture {

    private final Noise noise;

    public NoiseTexture() {
        noise = new PerlinNoise2D();
    }

    public NoiseTexture(Noise noise) {
        this.noise = noise;
    }

    @Override
    public Color getColor(Intercept intercept) {
        return Color.of(1, 1, 1).mul(.5f + .5f * noise.getValue(intercept.u, intercept.v));
    }

}
