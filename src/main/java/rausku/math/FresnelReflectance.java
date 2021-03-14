package rausku.math;

import static rausku.math.FloatMath.abs;
import static rausku.math.FloatMath.pow;

public class FresnelReflectance {

    private FresnelReflectance() {
        // statics only
    }

    /**
     * Compute the Fresnel reflectance at the interface between two dielectric (non-conductive) materials
     *
     * @param cosThetaI cosine of the incident angle
     * @param etaI      index of refraction in incident media
     * @param cosThetaT cosine of the transmitted angle
     * @param etaT      index of refraction in transmitted media
     * @return
     */
    public static float dielectric(float cosThetaI, float etaI, float cosThetaT, float etaT) {
        float parallel = (etaT * cosThetaI - etaI * cosThetaT) / (etaT * cosThetaI + etaI * cosThetaT);
        float perpendicular = (etaI * cosThetaI - etaT * cosThetaT) / (etaI * cosThetaI + etaT * cosThetaT);
        return .5f * (parallel * parallel + perpendicular * perpendicular);
    }

    public static float approximation(float etaT, float cosThetaI) {
        float R0 = pow((1 - etaT) / (1 + etaT), 2);
        return R0 + (1 - R0) * pow(1 - abs(cosThetaI), 5);
    }
}
