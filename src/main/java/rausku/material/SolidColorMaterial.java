package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

/**
 * Material of a single constant radiance
 */
public class SolidColorMaterial implements Material {
    /**
     * Base radiance of the object
     */
    private Color diffuseColor;
    /**
     * Color of specular reflection. Some materials such as plastics are covered with a thin glossy film that has a
     * colorless/white specular reflection.
     */
    private Color reflectiveColor;
    private float reflectiveness;
    private float indexOfRefraction = 1;

    protected SolidColorMaterial() {
    }

    protected SolidColorMaterial(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        this(diffuseColor, reflectiveColor, reflectiveness, 1);
    }

    protected SolidColorMaterial(Color diffuseColor, Color reflectiveColor, float reflectiveness, float indexOfRefraction) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
        this.indexOfRefraction = indexOfRefraction;
    }

    @Override
    public boolean hasRefraction() {
        return indexOfRefraction != 1;
    }

    @Override
    public float getIndexOfRefraction() {
        return indexOfRefraction;
    }

    @Override
    public BRDF getBSDF(Intercept intercept) {
        return new LambertianBRDF(diffuseColor);
    }

    public Color getDiffuseColor(Vec interceptPoint) {
        return diffuseColor;
    }
}
