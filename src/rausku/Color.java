package rausku;

import java.util.Objects;

import static java.lang.Math.exp;
import static java.lang.Math.min;

public class Color {
    float r;
    float g;
    float b;

    private Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color of(float r, float g, float b) {
        return new Color(r, g, b);
    }

    public int toIntRGB() {
        int r = min(255, (int) (256 * (1 - exp(-this.r))));
        int g = min(255, (int) (256 * (1 - exp(-this.g))));
        int b = min(255, (int) (256 * (1 - exp(-this.b))));
        return (0xff << 24) | (r << 16) | (g << 8) | b;
    }

    public Color mulAdd(float directionalLightEnergy, Color color, Color color1) {
        this.r = this.r * directionalLightEnergy * color.r + color1.r;
        this.g = this.g * directionalLightEnergy * color.g + color1.g;
        this.b = this.b * directionalLightEnergy * color.b + color1.b;
        return this;
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
        return "Color{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
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
