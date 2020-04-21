package rausku.geometry;

import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.Arrays;

import static rausku.math.FloatMath.sqrt;

public class QuadraticForm implements CSGObject, SceneObject {
    private final Matrix matrix;
    private final Matrix gradient;
    private final boolean zeroIsInside;
    private final Material material;

    public QuadraticForm(Matrix matrix, Material material) {
        this.matrix = matrix;
        this.gradient = getGradient(matrix);
        this.material = material;
        this.zeroIsInside = true;
    }

    public QuadraticForm(boolean zeroIsInside, Matrix matrix, Material material) {
        this.matrix = matrix;
        this.material = material;
        this.gradient = getGradient(matrix);
        this.zeroIsInside = zeroIsInside;
    }

    private static Matrix getGradient(Matrix matrix) {
        return Matrix.mul(Matrix.diag(1, 1, 1, 0), Matrix.plus(matrix, matrix.transpose()));
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {

        float[] floats = {Float.NaN, Float.NaN};

        Vec v1 = ray.getDirection();
        Vec v0 = ray.getOrigin();

        Vec transform0 = matrix.transform(v0);
        Vec transform1 = matrix.transform(v1);

        float A = v1.dot(transform1);
        float B = v1.dot(transform0) + v0.dot(transform1);
        float C = v0.dot(transform0);

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            float sqrt = Math.copySign(sqrt(determinant), A);
            floats[0] = (-B - sqrt) / (2 * A);
            floats[1] = (-B + sqrt) / (2 * A);
        }
        return floats;
    }

    public Intercept getIntercept(Ray ray) {
        Vec v0 = ray.getOrigin();
        Vec v1 = ray.getDirection();

        Vec transform0 = matrix.transform(v0);
        Vec transform1 = matrix.transform(v1);

        float A = v1.dot(transform1);
        float B = v1.dot(transform0) + v0.dot(transform1);
        float C = v0.dot(transform0);

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            float sqrtDeterminant = Math.copySign(sqrt(determinant), A);
            float intercept = (-B - sqrtDeterminant) / (2 * A);
            if (intercept > SceneObject.INTERCEPT_NEAR) {
                return new Intercept(intercept, ray.apply(intercept), null);
            }
            intercept = (-B + sqrtDeterminant) / (2 * A);
            if (intercept > SceneObject.INTERCEPT_NEAR) {
                return new Intercept(intercept, ray.apply(intercept), null);
            }
        }
        return Intercept.noIntercept();
    }

    public Vec getNormal(Ray ray, Intercept intercept) {
        if (zeroIsInside) {
            return gradient.transform(intercept.interceptPoint).normalize();
        } else {
            return gradient.transform(intercept.interceptPoint).normalize().mul(-1);
        }
    }

    public Material getMaterial() {
        return material;
    }
}
