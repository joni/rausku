package rausku.algorithm;

import rausku.lighting.Color;
import rausku.math.Ray;

public interface RayTracer {

    Color resolveRayColor(Ray ray);

    void setDebug(boolean b);
}
