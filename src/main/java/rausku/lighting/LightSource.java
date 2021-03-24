package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

public interface LightSource {

    Sample sample(SceneIntercept intercept, float s, float t);

    boolean intercepts(Ray ray);

    Color getColor();

    float getIntensity(Vec interceptPoint);

    class Sample {
        /**
         * Spectrum of the light
         */
        public final Color color;

        /**
         * Ray to the light source
         */
        public final Ray ray;

        /**
         * Likelihood of the sample (value of the probability distribution function)
         */
        public final float likelihood;

        public Sample(Color color, Ray ray, float likelihood) {
            this.color = color;
            this.ray = ray;
            this.likelihood = likelihood;
        }

        @Override
        public String toString() {
            return "Sample{" +
                    "color=" + color +
                    ", ray=" + ray +
                    ", likelihood=" + likelihood +
                    '}';
        }
    }
}
