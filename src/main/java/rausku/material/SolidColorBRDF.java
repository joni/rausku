package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

/**
 * A hacky BRDF that emulates a traditional (non-physically based) material that behaves differently for light coming
 * direct from light sources and for light reflecting from other objects.
 */
public class SolidColorBRDF implements BRDF {

    private final Color diffuseColor;
    private final Color reflectiveColor;
    private final float reflectiveness;

    public SolidColorBRDF(Color diffuseColor) {
        this(diffuseColor, diffuseColor, 0);
    }

    public SolidColorBRDF(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
    }

    @Override
    public Color evaluate(Vec outgoing, Vec incident) {
        return diffuseColor;
    }

    @Override
    public Sample sample(Vec outgoing, float s, float t) {
        Vec incident = Vec.J.reflected(outgoing);
        return new Sample(reflectiveColor.mul(reflectiveness / incident.y), incident, 1, true);
    }
}
