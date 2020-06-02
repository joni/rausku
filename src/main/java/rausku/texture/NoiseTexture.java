package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Noise2D;
import rausku.math.PerlinNoise2D;

public class NoiseTexture implements Texture {

    private final Noise2D noise;
    private final Color color1;
    private final Color color2;

    public NoiseTexture() {
        this(new PerlinNoise2D());
    }

    public NoiseTexture(Noise2D noise) {
        this(noise, Color.of(0, 0, 0), Color.of(1, 1, 1));
    }

    public NoiseTexture(Noise2D noise, Color color1, Color color2) {
        this.noise = noise;
        this.color1 = color1;
        this.color2 = color2;
    }

    @Override
    public Color getColor(Intercept intercept) {
        float noiseValue = .5f + .5f * noise.getValue(intercept.u, intercept.v);
        return Color.blend(1 - noiseValue, color1, noiseValue, color2);
    }

}
