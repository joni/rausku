package rausku.math;

@FunctionalInterface
public interface Noise {

    float getValue(float u, float v);


    default Noise fractalNoise(int octaves) {
        return (u, v) -> {
            float scale = 1;
            float value = 0;
            for (int i = 0; i < octaves; i++) {
                value += getValue(u * scale, v * scale) / scale;
                scale *= 2;
            }

            return value;
        };
    }
}
