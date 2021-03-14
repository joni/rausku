package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

import java.util.concurrent.ThreadLocalRandom;

public class CompositeBRFD implements BRDF {
    private final BRDF[] components;

    public CompositeBRFD(BRDF... components) {
        this.components = components;
    }

    @Override
    public Color evaluate(Vec outgoing, Vec incident) {
        Color color = Color.black();
        for (BRDF component : components) {
            color.add(component.evaluate(outgoing, incident));
        }
        return color;
    }

    @Override
    public Sample sample(Vec outgoing, float s, float t) {
        ThreadLocalRandom rng = ThreadLocalRandom.current();
        int componentIndex = rng.nextInt(components.length);
        return components[componentIndex].sample(outgoing, s, t);
    }
}
