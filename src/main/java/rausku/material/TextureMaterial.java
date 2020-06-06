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
    public float getReflectiveness() {
        return 0;
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
