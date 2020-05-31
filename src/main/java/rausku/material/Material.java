package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.texture.CheckerBoardTexture;
import rausku.texture.GinghamTexture;

public interface Material {
    static Material matte(Color color) {
        return new SolidColorMaterial(color, Color.of(0, 0, 0), 0);
    }

    static Material plastic(Color color, float reflectiveness) {
        // "Plastic" is covered by a thin, glossy reflective film that reflects the spectrum uniformly
        return new SolidColorMaterial(color, Color.of(1, 1, 1), reflectiveness);
    }

    static Material metallic(Color color, float reflectiveness) {
        // Metals are highly reflective but may absorb some wavelengths (think copper, gold)
        return new SolidColorMaterial(Color.of(0, 0, 0), color, reflectiveness);
    }

    static Material glass() {
        // Glass is highly reflective and allows transmitting light through the surface
        return new SolidColorMaterial(Color.of(0f, 0f, 0f), Color.of(1f, 1f, 1f), 1f, 1.5f);
    }

    static Material checkerBoard(float scale) {
        return new TextureMaterial(new CheckerBoardTexture(scale));
    }

    static Material checkerBoard(float scale, Color color) {
        return new TextureMaterial(new CheckerBoardTexture(scale, color));
    }

    static Material gingham(float scale, Color color1) {
        return new TextureMaterial(new GinghamTexture(scale, color1));
    }

    static Material gingham(float scale, Color color1, Color color2) {
        return new TextureMaterial(new GinghamTexture(scale, color1, color2));
    }

    static Material gingham(float scale, Color color1, Color color2, Color color3) {
        return new TextureMaterial(new GinghamTexture(scale, color1, color2, color3));
    }

    Color getDiffuseColor(Intercept intercept);

    float getReflectiveness();

    Color getReflectiveColor(Intercept intercept);

    boolean hasRefraction();

    float getIndexOfRefraction();
}
