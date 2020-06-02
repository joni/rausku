package rausku.math;

@FunctionalInterface
public interface Noise2D {

    float getValue(float u, float v);

    default Noise2D fractalNoise(int octaves) {
        return (u, v) -> {
            float scale = 1;
            float value = 0;
            for (int i = 0; i < octaves; i++) {
                value = 2 * value + getValue(u * scale, v * scale);
                scale *= 2;
            }

            return value / (scale - 1);
        };
    }
}
