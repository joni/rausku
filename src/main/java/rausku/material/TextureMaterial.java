package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.texture.Texture;

public class TextureMaterial implements Material {

    Texture diffuseTexture;
    Texture specularTexture;

    public TextureMaterial(Texture diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    @Override
    public boolean hasRefraction() {
        return false;
    }

    @Override
    public float getIndexOfRefraction() {
        return 1;
    }

    @Override
    public BRDF getBSDF(Intercept intercept) {
        return new LambertianBRDF(diffuseTexture.getColor(intercept));
    }

    @Override
    public boolean hasSpecularReflection() {
        return false;
    }

    @Override
    public Color getDiffuseColor(Intercept intercept) {
        return diffuseTexture.getColor(intercept);
    }

    @Override
    public Color getReflectiveColor(Intercept intercept) {
        return specularTexture.getColor(intercept);
    }

}
