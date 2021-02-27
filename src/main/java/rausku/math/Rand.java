package rausku.math;

import static rausku.math.FloatMath.*;

public class Rand {

    /**
     * Returns a uniformly sampled vector on the +Z hemisphere
     *
     * @param s number 0-1
     * @param t number 0-1
     * @return a vector on the +Z hemisphere
     */
    public static Vec hemisphere(float s, float t) {
        float r = sqrt(1 - s * s);
        float u = 2 * PI * t;
        return Vec.of(r * cos(u), r * sin(u), s);
    }

    /**
     * Returns a cosine-weighted vector on the +Z hemisphere
     *
     * @param s number 0-1
     * @param t number 0-1
     * @return a vector on the +Z hemisphere
     */
    public static Vec cosineHemisphere(float s, float t) {
        float r = sqrt(s);
        float u = 2 * PI * t;
        float x = r * cos(u);
        float y = r * sin(u);
        return Vec.of(x, y, sqrt(1 - x * x - y * y));
    }
}
