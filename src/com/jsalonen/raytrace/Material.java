package com.jsalonen.raytrace;

import com.jsalonen.raytrace.geometry.Intercept;
import com.jsalonen.raytrace.math.Vec;

public class Material {
    private Color diffuseColor;
    private Color reflectiveColor;
    private float reflectiveness;
    private float transparency;
    private float indexOfRefraction;

    public Material(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
        this.reflectiveness = 0f;
    }

    public Material(Color reflectiveColor, float reflectiveness) {
        this.diffuseColor = Color.of(.1f, .1f, .1f);
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
    }

    public Material(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
    }

    public Material(Color diffuseColor, Color reflectiveColor, float reflectiveness, float transparency, float indexOfRefraction) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
        this.transparency = transparency;
        this.indexOfRefraction = indexOfRefraction;
    }

    public static Material plastic(Color color, float reflectiveness) {
        return new Material(color, color, reflectiveness);
    }

    public static Material metallic(Color color, float reflectiveness) {
        return new Material(Color.of(.01f, .01f, .01f), color, reflectiveness);
    }

    public static Material glass() {
        return new Material(Color.of(0.01f, .01f, .01f), Color.of(1f, 1f, 1f), 0f, .99f, 1.5f);
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
        return transparency > 0;
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
