package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public interface SceneObject {
    double INTERCEPT_NEAR = 1e-3;

    Vec getNormal(Ray ray, Intercept interceptPoint);

    Intercept getIntercept(Ray ray);

    BoundingBox getBoundingBox();
}
