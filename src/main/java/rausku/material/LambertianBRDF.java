package rausku.material;

import rausku.lighting.Color;
import rausku.math.Rand;
import rausku.math.Vec;

public class LambertianBRDF implements BRDF {

    final Color value;

    public LambertianBRDF(Color value) {
        this.value = value;
    }

    public Color evaluate(Vec outgoingDirection, Vec incidentDirection) {
        return value;
    }

    public Sample sample(Vec outgoingDirection, float s, float t) {
        Vec hemisphere = Rand.cosineHemisphere(s, t);
        return new Sample(value, hemisphere, hemisphere.y(), false);
    }
}
