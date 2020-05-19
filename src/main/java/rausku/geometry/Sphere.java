package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import static java.lang.Math.sqrt;

public class Sphere implements CSGObject, SceneObject {
    private Vec center;
    private float radius;
    private Material material;

    public Sphere(Vec center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public boolean intersectsRay(Ray ray) {
        return getIntercept0(ray) > 1e-6;
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        float[] floats = {Float.NaN, Float.NaN};

        Vec a = ray.direction;
        Vec b = ray.origin;
        Vec c = this.center;
        float r = this.radius;

        Vec bMINUSc = b.sub(c);

        float A = a.sqLen();
        float B = 2 * a.dot(bMINUSc);
        float C = bMINUSc.sqLen() - r * r;

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            floats = new float[]{(-B - (float) sqrt(determinant)) / (2 * A), (-B + (float) sqrt(determinant)) / (2 * A)};
        }
        return floats;
    }

    public Intercept getIntercept(Ray ray) {
        float intercept = getIntercept0(ray);
        return new Intercept(intercept, ray.apply(intercept), null);
    }

    private float getIntercept0(Ray ray) {
        Vec a = ray.direction;
        Vec b = ray.origin;
        Vec c = this.center;
        float r = this.radius;

        Vec bMINUSc = b.sub(c);

        float A = a.sqLen();
        float B = 2 * a.dot(bMINUSc);
        float C = bMINUSc.sqLen() - r * r;

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            float intercept = (-B - (float) sqrt(determinant)) / (2 * A);
            if (intercept > SceneObject.INTERCEPT_NEAR) {
                return intercept;
            }
            intercept = (-B + (float) sqrt(determinant)) / (2 * A);
            if (intercept > SceneObject.INTERCEPT_NEAR) {
                return intercept;
            }
        }
        return Float.NaN;
    }

    public Vec getNormal(Ray ray, Intercept intercept) {
        return intercept.interceptPoint.sub(center).normalize();
    }

    public Material getMaterial() {
        return material;
    }
}
