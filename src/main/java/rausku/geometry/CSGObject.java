package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public interface CSGObject {

    Intercept[] getAllInterceptObjects(Ray ray);

    Vec getNormal(Intercept intercept);

    BoundingBox getBoundingBox();
}
