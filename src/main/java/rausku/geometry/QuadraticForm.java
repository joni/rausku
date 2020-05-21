package rausku.geometry;

import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.sqrt;

public class QuadraticForm implements CSGObject, SceneObject {
    private final Matrix matrix;
    private final Matrix gradient;
    private final boolean zeroIsInside;

    public QuadraticForm(Matrix matrix) {
        this.matrix = matrix;
        this.gradient = getGradient(matrix);
        this.zeroIsInside = true;
    }

    public QuadraticForm(boolean zeroIsInside, Matrix matrix) {
        this.matrix = matrix;
        this.gradient = getGradient(matrix);
        this.zeroIsInside = zeroIsInside;
    }

    private static Matrix getGradient(Matrix matrix) {
        return Matrix.mul(Matrix.diag(1, 1, 1, 0), Matrix.plus(matrix, matrix.transpose()));
    }

    public static QuadraticForm createSphere(Vec center, float radius) {
        return new QuadraticForm(Matrix.of(
                1, 0, 0, -center.x,
                0, 1, 0, -center.y,
                0, 0, 1, -center.z,
                -center.x, -center.y, -center.z, center.sqLen() - radius * radius));
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {

        float[] floats = {Float.NaN, Float.NaN};

        Vec v1 = ray.direction;
        Vec v0 = ray.origin;

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
        Vec v0 = ray.origin;
        Vec v1 = ray.direction;

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
}
