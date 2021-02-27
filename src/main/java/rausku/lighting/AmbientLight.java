package rausku.lighting;

import rausku.geometry.Intercept;
import rausku.math.Ray;
import rausku.math.Vec;

public class AmbientLight implements LightSource {
    private Color color;

    public AmbientLight(Color color) {
        this.color = color;
    }

    @Override
    public Ray sampleRay(Intercept intercept) {
        return null;
    }

    @Override
    public boolean intercepts(Ray ray) {
        return true;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }
}
