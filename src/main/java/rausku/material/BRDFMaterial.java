package rausku.material;

import rausku.geometry.Intercept;

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
