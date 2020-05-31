package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

import java.util.Random;

import static rausku.math.FloatMath.*;

public class NoiseTexture implements Texture {

    public static final int SIZE = 256;
    private final float scale;
    private final float[][][] gradient;

    public NoiseTexture(float scale) {
        this.gradient = new float[SIZE][SIZE][2];
        this.scale = scale;

        Random rnd = new Random(42);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                float angle0 = toRadians(rnd.nextInt(360));
                gradient[i][j][0] = cos(angle0);
                gradient[i][j][1] = sin(angle0);
            }
        }
    }

    float getGradient(int U, int V, float u, float v) {
        float[] floats = gradient[U & (SIZE - 1)][V & (SIZE - 1)];
        return (u - U) * floats[0] + (v - V) * floats[1];
    }

    float interpolate(float start, float end, float x) {
        float blend = x * x * x * (10 - x * (15 - x * 6));
        return start * (1 - blend) + end * blend;
    }

    @Override
    public Color getColor(Intercept intercept) {
        float u = intercept.u * scale;
        float v = intercept.v * scale;

        int u0 = floor(u);
        int v0 = floor(v);
        int u1 = u0 + 1;
        int v1 = v0 + 1;
        float n0 = getGradient(u0, v0, u, v);
        float n1 = getGradient(u1, v0, u, v);
        float n2 = getGradient(u0, v1, u, v);
        float n3 = getGradient(u1, v1, u, v);

        float fracU = u - u0;
        float fracV = v - v0;
//        float c0 = interpolate(n0, n1, fracU);
//        float c1 = interpolate(n2, n3, fracU);
//        float c = interpolate(c0, c1, fracV);
        float c0 = interpolate(n0, n2, fracV);
        float c1 = interpolate(n1, n3, fracV);
        float c = interpolate(c0, c1, fracU);

        return Color.of(1, 1, 1).mul(.5f + .5f * c);
    }
}
