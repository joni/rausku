package rausku.lighting;

import rausku.math.Ray;
import rausku.math.Vec;

public interface LightSource {
    Ray getRay(Vec origin);

    boolean intercepts(Ray ray);

    Color getColor();
}
