package rausku;

public interface RayTracer {

    double INTERCEPT_NEAR = 1e-3;

    Color resolveRayColor(float reflectiveness, Ray ray);
}
