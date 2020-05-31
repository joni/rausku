package rausku.lighting;

import java.util.Objects;

import static java.lang.Math.exp;
import static java.lang.Math.min;

public class Color {
    public final float r;
    public final float g;
    public final float b;

    private Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
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

    public static Color ofHsl(float h, float s, float l) {
        java.awt.Color rgb = java.awt.Color.getHSBColor(h, s, l);
        return Color.of(rgb.getRed() / 255f, rgb.getGreen() / 255f, rgb.getBlue() / 255f);
    }

    public int toIntRGB() {
        int r = min(255, (int) (256 * (1 - exp(-this.r))));
        int g = min(255, (int) (256 * (1 - exp(-this.g))));
        int b = min(255, (int) (256 * (1 - exp(-this.b))));
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

    public Color mul(float c) {
        return new Color(r * c, g * c, b * c);
    }


    @Override
    public String toString() {
        return String.format("Color{r=%s, g=%s, b=%s}", r, g, b);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Float.compare(color.r, r) == 0 &&
                Float.compare(color.g, g) == 0 &&
                Float.compare(color.b, b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }
}
