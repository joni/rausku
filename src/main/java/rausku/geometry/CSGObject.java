package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public interface CSGObject {
    float[] getAllIntercepts(Ray ray);

    Vec getNormal(Intercept intercept);

    BoundingBox getBoundingBox();
}
