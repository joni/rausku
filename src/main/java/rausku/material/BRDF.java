package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

/**
 * Bi-directional Reflection Distribution Function tells you how much radiance leaves the surface in a given direction
 * as a result of incident radiance
 */
public interface BRDF {

    /**
     * Evaluate the distribution of incident light to outgoing light.
     *
     * @param outgoingDirection direction of outgoing light
     * @param incidentDirection direction of incident light
     * @return the value of the distribution function
     */
    Color evaluate(Vec outgoingDirection, Vec incidentDirection);

    /**
     * Sample the reflection distribution function for a given outgoing direction.
     *
     * @param outgoingDirection direction of outgoing light
     * @param s                 random number 0-1 to choose the sample
     * @param t                 random number 0-1 to choose the sample
     * @return a sample of the distribution
     */
    Sample sample(Vec outgoingDirection, float s, float t);

    record Sample(Color value, Vec incidentDirection, float likelihood, boolean isSpecular) {
    }
}
