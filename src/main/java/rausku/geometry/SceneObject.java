package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

public interface SceneObject {
    double INTERCEPT_NEAR = 1e-3;

    Vec getNormal(Ray ray, Intercept interceptPoint);

    Material getMaterial();

    Intercept getIntercept(Ray ray);

}
