package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

public class Material {
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

    protected Material() {
    }

    protected Material(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        this(diffuseColor, reflectiveColor, reflectiveness, 1);
    }

    protected Material(Color diffuseColor, Color reflectiveColor, float reflectiveness, float indexOfRefraction) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
        this.indexOfRefraction = indexOfRefraction;
    }

    public static Material matte(Color color) {
        return new Material(color, Color.of(0, 0, 0), 0);
    }

    public static Material plastic(Color color, float reflectiveness) {
        // "Plastic" is covered by a thin, glossy reflective film that reflects the spectrum uniformly
        return new Material(color, Color.of(1, 1, 1), reflectiveness);
    }

    public static Material metallic(Color color, float reflectiveness) {
        // Metals are highly reflective but may absorb some wavelengths (think copper, gold)
        return new Material(Color.of(0, 0, 0), color, reflectiveness);
    }

    public static Material glass() {
        // Glass is highly reflective and allows transmitting light through the surface
        return new Material(Color.of(0f, 0f, 0f), Color.of(1f, 1f, 1f), 1f, 1.5f);
    }

    public float getReflectiveness() {
        return reflectiveness;
    }

    public Color getDiffuseColor(Vec interceptPoint) {
        return diffuseColor;
    }

    public Color getReflectiveColor() {
        return reflectiveColor;
    }

    public boolean hasRefraction() {
        return indexOfRefraction != 1;
    }

    public float getIndexOfRefraction() {
        return indexOfRefraction;
    }

    public Color getDiffuseColor(Intercept intercept) {
//        if (intercept.info instanceof Polygon) {
//            return ((Polygon) intercept.info).getColor(intercept.interceptPoint);
//        } else {
        return getDiffuseColor(intercept.interceptPoint);
//        }
    }
}
