package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

/**
 * Represents a the shape of an object
 */
public interface Geometry {
    float INTERCEPT_NEAR = 1e-3f;

    BoundingBox getBoundingBox();

    Intercept getIntercept(Ray ray);

    default boolean hasIntercept(Ray ray) {
        var intercept = getIntercept(ray);
        return intercept.isValid() && intercept.intercept < ray.bound;
    }

    Vec getNormal(Intercept intercept);
}
