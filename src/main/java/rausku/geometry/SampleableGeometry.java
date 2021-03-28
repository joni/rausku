package rausku.geometry;

import rausku.math.Vec;

/**
 * Shape from which we can sample points. The main use case is for area lights.
 */
public interface SampleableGeometry extends Geometry {

    /**
     * Sample the surface of the geometry.
     *
     * @param s
     * @param t
     * @return
     */
    Sample sample(float s, float t);

    record Sample(Vec point, Vec normal, float likelihood) {
    }
}
