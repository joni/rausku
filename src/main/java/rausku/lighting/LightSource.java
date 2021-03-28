package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

/**
 * Representation of a source of light.
 */
public interface LightSource {

    /**
     * Sample the light source
     *
     * @param intercept
     * @param s
     * @param t
     * @return a sample of the light source
     */
    Sample sample(SceneIntercept intercept, float s, float t);

    /**
     * Returns the recommended number of samples
     *
     * @return
     */
    default int getSampleCount() {
        return 1;
    }

    /**
     * Check if the ray intercepts this light source
     *
     * @param ray the ray to check
     * @return true iff the ray hits this light source
     */
    boolean hasIntercept(Ray ray);

    /**
     * Returns the radiance arriving from the light source to the intercept point.
     *
     * @return radiance
     */
    Color evaluate();

    float getIntensity(Vec interceptPoint);

    record Sample(Color radiance, Ray rayToLight, float likelihood) {
    }
}
