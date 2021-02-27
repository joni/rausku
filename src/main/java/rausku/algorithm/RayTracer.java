package rausku.algorithm;

import rausku.lighting.Color;
import rausku.math.Ray;

public interface RayTracer {

    default Color resolveRayColor(Ray ray) {
        return resolveRayColor(ray, 0);
    }

    Color resolveRayColor(Ray ray, int depth);

    void setDebug(boolean b);
}
