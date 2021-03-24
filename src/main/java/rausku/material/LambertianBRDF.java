package rausku.material;

import rausku.lighting.Color;
import rausku.math.Rand;
import rausku.math.Vec;

public class LambertianBRDF implements BRDF {

    final Color color;

    public LambertianBRDF(Color color) {
        this.color = color;
    }

    public Color evaluate(Vec outgoing, Vec incident) {
        return color;
    }

    public Sample sample(Vec outgoing, float s, float t) {
        Vec hemisphere = Rand.cosineHemisphere(s, t);
        return new Sample(color, hemisphere, hemisphere.y());
    }
}
