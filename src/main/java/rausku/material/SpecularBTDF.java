package rausku.material;

import rausku.lighting.Color;
import rausku.math.FresnelReflectance;
import rausku.math.Vec;

/**
 * Specular transmission
 */
public class SpecularBTDF implements BRDF {
    /**
     * Index of refraction
     */
    private final float r;

    public SpecularBTDF(float r) {
        this.r = r;
    }

    @Override
    public Color evaluate(Vec outgoing, Vec incident) {
        return Color.black();
    }

    @Override
    public Sample sample(Vec outgoing, float s, float t) {
        Vec incidentT = Vec.J.refracted(outgoing, r);
        Vec incidentR = Vec.J.reflected(outgoing);

        if (incidentT == null) {
            // total internal reflection
            return new Sample(Color.of(1 / incidentR.y), incidentR, 1, true);
        }

        float reflectance;
        if (incidentT.y > 0) {
            // ray exits material
            reflectance = FresnelReflectance.dielectric(incidentT.y, r, outgoing.y, 1);
        } else {
            // ray enters material
            reflectance = FresnelReflectance.dielectric(-incidentT.y, 1, -outgoing.y, r);
        }

        if (s < reflectance) {
            // pick reflected ray
            return new Sample(Color.of(reflectance / incidentR.y), incidentR, reflectance, true);
        }

        // pick transmitted ray
        float transmittance = 1 - reflectance;
        if (incidentT.y > 0) {
            // ray exits material
            float absCosTheta = incidentT.y;
            Color color = Color.of(transmittance / absCosTheta);
            return new Sample(color, incidentT, transmittance, true);
        } else {
            // ray enters material
            float absCosTheta = -incidentT.y;
            Color color = Color.of(transmittance / absCosTheta);
            return new Sample(color, incidentT, transmittance, true);
        }
    }
}
