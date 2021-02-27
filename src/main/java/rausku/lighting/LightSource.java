package rausku.lighting;

import rausku.geometry.Intercept;
import rausku.math.Ray;
import rausku.math.Vec;

public interface LightSource {
    Ray sampleRay(Intercept intercept);

    boolean intercepts(Ray ray);

    Color getColor();

    float getIntensity(Vec interceptPoint);
}
