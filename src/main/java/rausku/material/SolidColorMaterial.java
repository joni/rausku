package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

/**
 * Material of a single constant color
 */
public class SolidColorMaterial implements Material {
    /**
     * Base color of the object
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
    public float getReflectiveness() {
        return reflectiveness;
    }

    @Override
    public Color getReflectiveColor(Intercept intercept) {
        return reflectiveColor;
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
    public Color getDiffuseColor(Intercept intercept) {
//        if (intercept.info instanceof Polygon) {
//            return ((Polygon) intercept.info).getColor(intercept.interceptPoint);
//        } else {
        return getDiffuseColor(intercept.interceptPoint);
//        }
    }

    public Color getDiffuseColor(Vec interceptPoint) {
        return diffuseColor;
    }
}
