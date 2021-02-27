package rausku.geometry;

import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.sqrt;

public class QuadraticForm implements CSGObject, SceneObject {

    private static final float[] NO_INTERCEPT = {};

    private final Matrix matrix;
    private final Matrix gradient;

    private final BoundingBox bbox;

    public QuadraticForm(Matrix matrix) {
        this(matrix, BoundingBox.unbounded());
    }

    public QuadraticForm(Matrix matrix, BoundingBox bbox) {
        this.matrix = matrix;
        this.gradient = getGradient(matrix);
        this.bbox = bbox;
    }

    public QuadraticForm(boolean zeroIsInside, Matrix matrix) {
        if (zeroIsInside) {
            this.matrix = matrix;
        } else {
            this.matrix = Matrix.mul(Matrix.diag(-1), matrix);
        }
        this.gradient = getGradient(this.matrix);
        this.bbox = BoundingBox.unbounded();
    }

    private static Matrix getGradient(Matrix matrix) {
        return Matrix.mul(Matrix.diag(1, 1, 1, 0), Matrix.plus(matrix, matrix.transpose()));
    }

    public static QuadraticForm createSphere(Vec center, float radius) {
        Vec radiusVec = Vec.of(radius, radius, radius);
        return new QuadraticForm(Matrix.of(
                1, 0, 0, -center.x,
                0, 1, 0, -center.y,
                0, 0, 1, -center.z,
                -center.x, -center.y, -center.z, center.sqLen() - radius * radius),
                new BoundingBox(center.sub(radiusVec), center.add(radiusVec)));
    }

    @Override
    public float[] getAllIntercepts(Ray ray) {
        if (!bbox.testIntercept(ray)) {
            return NO_INTERCEPT;
        }
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
            return floats;
        } else {
            return NO_INTERCEPT;
        }
    }

    public Intercept getIntercept(Ray ray) {
        if (!bbox.testIntercept(ray)) {
            return Intercept.noIntercept();
        }
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
            Vec interceptPoint = ray.apply(intercept);
            if (intercept > SceneObject.INTERCEPT_NEAR && bbox.contains(interceptPoint)) {
                float phi = (float) Math.atan2(interceptPoint.z, interceptPoint.x);
                float r = interceptPoint.len();
                float theta = (float) Math.acos(interceptPoint.y / r);
                return new Intercept(intercept, interceptPoint, phi, theta, interceptPoint);
            }
            intercept = (-B + sqrtDeterminant) / (2 * A);
            interceptPoint = ray.apply(intercept);
            if (intercept > SceneObject.INTERCEPT_NEAR && bbox.contains(interceptPoint)) {
                float phi = (float) Math.atan2(interceptPoint.z, interceptPoint.x);
                float r = interceptPoint.len();
                float theta = (float) Math.acos(interceptPoint.y / r);
                return new Intercept(intercept, interceptPoint, phi, theta, interceptPoint);
            }
        }
        return Intercept.noIntercept();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bbox;
    }

    public Vec getNormal(Intercept intercept) {
        Vec interceptPoint = (Vec) intercept.info;
        return gradient.transform(interceptPoint).normalize();
    }
}
