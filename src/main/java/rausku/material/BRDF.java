package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

/**
 * Bi-directional Reflection Distribution Function tells you how much radiance leaves the surface in a given direction
 * as a result of incident radiance
 */
public interface BRDF {

    Color evaluate(Vec outgoing, Vec incident);

    Sample sample(Vec outgoing, float s, float t);

    class Sample {
        /**
         * Spectrum
         */
        public final Color color;

        /**
         * Incident light direction
         */
        public final Vec incident;

        /**
         * Likelihood of incident light direction
         */
        public final float likelihood;

        public Sample(Color color, Vec incident, float likelihood) {
            this.color = color;
            this.incident = incident;
            this.likelihood = likelihood;
        }
    }
}
