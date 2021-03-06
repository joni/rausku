package rausku.lighting;

import static java.lang.Math.exp;
import static java.lang.Math.max;
import static rausku.math.FloatMath.clamp;

/**
 * Representation of spectrum of light
 */
public record Color(float r, float g, float b) {

    private static final Color BLACK = Color.of(0f);

    public static Color of(float gray) {
        return new Color(gray, gray, gray);
    }

    public static Color of(float r, float g, float b) {
        return new Color(r, g, b);
    }

    public static Color average(Color[] colors) {
        float r = 0;
        float g = 0;
        float b = 0;

        for (Color color : colors) {
            r += color.r;
            g += color.g;
            b += color.b;
        }
        return Color.of(r / colors.length, g / colors.length, b / colors.length);
    }

    public static Color blend(float x, Color color1, float y, Color color2) {
        float r = x * color1.r + y * color2.r;
        float g = x * color1.g + y * color2.g;
        float b = x * color1.b + y * color2.b;
        return Color.of(r, g, b);
    }

    public static Color ofHsl(float h, float s, float l) {
        java.awt.Color rgb = java.awt.Color.getHSBColor(h, s, l);
        return Color.of(rgb.getRed() / 255f, rgb.getGreen() / 255f, rgb.getBlue() / 255f);
    }

    public static Color ofRgb(int rgb) {
        float r = (rgb & 0xff0000) >> 16;
        float g = (rgb & 0x00ff00) >> 8;
        float b = (rgb & 0x0000ff);
        return Color.of(r / 255f, g / 255f, b / 255f);
    }

    public static Color black() {
        return BLACK;
    }

    public boolean isBlack() {
        return this.r == 0f && this.g == 0f && this.b == 0f;
    }

    public int toIntRGB() {
        int r = clamp(0, 255, (int) (256 * (1 - exp(-this.r))));
        int g = clamp(0, 255, (int) (256 * (1 - exp(-this.g))));
        int b = clamp(0, 255, (int) (256 * (1 - exp(-this.b))));
        return (0xff << 24) | (r << 16) | (g << 8) | b;
    }

    public Color mulAdd(float scalar, Color color) {
        float r = scalar * this.r + color.r;
        float g = scalar * this.g + color.g;
        float b = scalar * this.b + color.b;
        return Color.of(r, g, b);
    }

    public Color add(Color color) {
        return Color.of(r + color.r, g + color.g, b + color.b);
    }

    public Color mul(Color color) {
        return Color.of(r * color.r, g * color.g, b * color.b);
    }

    public Color mul(float scalar) {
        return Color.of(r * scalar, g * scalar, b * scalar);
    }

    public Color div(float scalar) {
        return Color.of(r / scalar, g / scalar, b / scalar);
    }

    public float norm() {
        return max(max(r, g), b);
    }

    @Override
    public String toString() {
        return String.format("<%.2f %.2f %.2f>", r, g, b);
    }
}
