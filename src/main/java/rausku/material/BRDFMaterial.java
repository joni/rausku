package rausku.material;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

/**
 * Material that has the same BRDF at all points
 */
public class BRDFMaterial implements Material {

    public final BRDF bsdf;

    public BRDFMaterial(BRDF brdf) {
        this.bsdf = brdf;
    }

    public BRDFMaterial(BRDF... brdfs) {
        this.bsdf = new CompositeBRFD(brdfs);
    }

    @Override
    public Color getDiffuseColor(Intercept intercept) {
        return Color.black();
    }

    @Override
    public boolean hasSpecularReflection() {
        return true;
    }

    @Override
    public Color getReflectiveColor(Intercept intercept) {
        return Color.black();
    }

    @Override
    public boolean hasRefraction() {
        return false;
    }

    @Override
    public float getIndexOfRefraction() {
        return 1;
    }


    @Override
    public BRDF getBSDF(Intercept intercept) {
        return bsdf;
    }
}
