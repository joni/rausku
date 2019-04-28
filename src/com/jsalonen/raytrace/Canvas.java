package com.jsalonen.raytrace;

import com.jsalonen.raytrace.geometry.Vec;

class Canvas {

    private float sceneWidth;
    private float sceneHeight;
    private float dotsPerUnit;
    private Vec origin;
    private float distance;

    private int pixelWidth;
    private int pixelHeight;

    public Canvas(int pixelWidth, int pixelHeight, float dotsPerUnit, Vec origin, float distance) {
        this.sceneWidth = pixelWidth / dotsPerUnit;
        this.sceneHeight = pixelHeight / dotsPerUnit;
        this.dotsPerUnit = dotsPerUnit;
        this.origin = origin;
        this.distance = distance;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    public Ray getRayFromOriginToCanvas(int pixelX, int pixelY) {
        float canvasX = (pixelX - pixelWidth / 2) / dotsPerUnit;
        float canvasY = (pixelHeight / 2 - pixelY) / dotsPerUnit;
        float canvasZ = distance;
        Vec canvasPoint = Vec.of(canvasX, canvasY, canvasZ);
        return Ray.from(origin, canvasPoint);
    }
}
