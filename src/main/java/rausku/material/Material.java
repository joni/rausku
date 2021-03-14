package rausku.material;

import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.math.Vec;
import rausku.texture.CheckerBoardTexture;
import rausku.texture.GinghamTexture;

public interface Material {
    static Material matte(Color color) {
        return new BRDFMaterial(new LambertianBRDF(color));
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
        return new BRDFMaterial(new SpecularBTDF(1.5f));
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

    static Material solidColorMatte(Color color) {
        return new BRDFMaterial(new SolidColorBRDF(color));
    }

    static Material solidColor(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        return new BRDFMaterial(new SolidColorBRDF(diffuseColor, reflectiveColor, reflectiveness));
    }

    static Material mirror() {
        return new BRDFMaterial(new SpecularBRDF(Color.of(1f)));
    }

    Color getDiffuseColor(Intercept intercept);

    boolean hasSpecularReflection();

    Color getReflectiveColor(Intercept intercept);

    boolean hasRefraction();

    float getIndexOfRefraction();

    default Vec getNormal(Intercept intercept, SceneObject sceneObject) {
        return sceneObject.getNormal(intercept);
    }

    BRDF getBSDF(Intercept intercept);
}
