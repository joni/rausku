package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

public class SpecularBRDF implements BRDF {

    private final Color color;

    public SpecularBRDF(Color color) {
        this.color = color;
    }

    @Override
    public Color evaluate(Vec outgoing, Vec incident) {
        return Color.black();
    }

    @Override
    public Sample sample(Vec outgoing, float s, float t) {
        Vec incident = Vec.J.reflected(outgoing);
        return new Sample(color.mul(1 / incident.y), incident, 1, true);
    }
}
