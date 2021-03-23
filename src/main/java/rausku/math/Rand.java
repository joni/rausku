package rausku.math;

import static rausku.math.FloatMath.*;

public class Rand {

    /**
     * Returns a uniformly sampled vector from a sphere
     *
     * @param s number 0-1
     * @param t number 0-1
     * @return a unit vector
     */
    public static Vec sphere(float s, float t) {
        float p = 1 - 2 * s;
        float r = sqrt(1 - p * p);
        float q = 2 * PI * t;
        return Vec.of(r * cos(q), r * sin(q), p);
    }

    /**
     * Returns a uniformly sampled vector on the +Y hemisphere
     *
     * @param s number 0-1
     * @param t number 0-1
     * @return a vector on the +Y hemisphere
     */
    public static Vec hemisphere(float s, float t) {
        float r = sqrt(1 - s * s);
        float u = 2 * PI * t;
        return Vec.of(r * cos(u), s, r * sin(u));
    }

    /**
     * Returns a cosine-weighted vector on the +Y hemisphere
     *
     * @param s number 0-1
     * @param t number 0-1
     * @return a vector on the +Y hemisphere
     */
    public static Vec cosineHemisphere(float s, float t) {
        float r = sqrt(s);
        float u = 2 * PI * t;
        float x = r * cos(u);
        float y = r * sin(u);
        return Vec.of(x, sqrt(1 - x * x - y * y), y);
    }

    /**
     * Generates a uniformly sampled vector within a {@code theta}-environment around the +Y axis
     *
     * @param s     number 0-1
     * @param t     number 0-1
     * @param theta spherical angle
     * @return a vector around the +Y axis
     */
    public static Vec uniformCone(float s, float t, float theta) {
        return hemisphere(1 - s * theta / (2 * PI), t);
    }

    public static Vec uniformDisk(float s, float t) {
        float r = sqrt(s);
        float u = 2 * PI * t;
        return Vec.of(r * cos(u), 0, r * sin(u));
    }
}
