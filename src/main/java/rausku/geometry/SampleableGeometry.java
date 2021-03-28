package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

public interface SampleableGeometry extends Geometry {

    /**
     * Returns the area of the surface of the geometry.
     *
     * @return
     */
    float area();

    /**
     * Return a point on the surface of the geometry. It is assumed points are sampled uniformly, so that the likelihood
     * of any point is 1/area.
     *
     * @param s
     * @param t
     * @return
     */
    Vec sample(float s, float t);

    boolean hasIntercept(Ray ray);
}
