package rausku.algorithm;

import rausku.lighting.Color;
import rausku.math.Ray;
import rausku.scenes.Scene;

public class MonteCarloRayTracer implements RayTracer {

    private boolean debug;
    private Scene scene;

    public MonteCarloRayTracer(Scene scene) {
        this.scene = scene;
    }

    @Override
    public Color resolveRayColor(float reflectiveness, Ray ray) {
        return null;
    }

    @Override
    public void setDebug(boolean enable) {
        this.debug = enable;
    }
}
