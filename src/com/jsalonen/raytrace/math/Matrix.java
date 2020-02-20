package com.jsalonen.raytrace.math;

import com.jsalonen.raytrace.Ray;

import java.util.Objects;
import java.util.Random;

import static com.jsalonen.raytrace.math.FloatMath.cos;
import static com.jsalonen.raytrace.math.FloatMath.sin;

public class Matrix {

    private static final Matrix EYE = diag(1f);

    private final float f11;
    private final float f12;
    private final float f13;
    private final float f14;
    private final float f21;
    private final float f22;
    private final float f23;
    private final float f24;
    private final float f31;
    private final float f32;
    private final float f33;
    private final float f34;
    private final float f41;
    private final float f42;
    private final float f43;
    private final float f44;

    private Matrix(float f11, float f12, float f13, float f14,
                   float f21, float f22, float f23, float f24,
                   float f31, float f32, float f33, float f34,
                   float f41, float f42, float f43, float f44) {

        this.f11 = f11;
        this.f12 = f12;
        this.f13 = f13;
        this.f14 = f14;
        this.f21 = f21;
        this.f22 = f22;
        this.f23 = f23;
        this.f24 = f24;
        this.f31 = f31;
        this.f32 = f32;
        this.f33 = f33;
        this.f34 = f34;
        this.f41 = f41;
        this.f42 = f42;
        this.f43 = f43;
        this.f44 = f44;
    }

    private Matrix(float f) {
        f11 = f22 = f33 = f44 = f;
        f12 = f13 = f14 = 0;
        f21 = f23 = f24 = 0;
        f31 = f32 = f34 = 0;
        f41 = f42 = f43 = 0;
    }

    public static Matrix eye() {
        return EYE;
    }

    public Matrix transpose() {
        return Matrix.of(
                f11, f21, f31, f41,
                f12, f22, f32, f42,
                f13, f23, f33, f43,
                f14, f24, f34, f44
        );
    }

    public Matrix inverse() {

        float g11 = f11, g12 = f12, g13 = f13, g14 = f14,
                g21 = f21, g22 = f22, g23 = f23, g24 = f24,
                g31 = f31, g32 = f32, g33 = f33, g34 = f34,
                g41 = f41, g42 = f42, g43 = f43, g44 = f44;

        float h11 = 1, h12 = 0, h13 = 0, h14 = 0,
                h21 = 0, h22 = 1, h23 = 0, h24 = 0,
                h31 = 0, h32 = 0, h33 = 1, h34 = 0,
                h41 = 0, h42 = 0, h43 = 0, h44 = 1;

        // first phase: going down

        // divide first row with g11
        g12 /= g11;
        g13 /= g11;
        g14 /= g11;
        h11 /= g11;
        g11 = 1;

        // make rest of first column go to zero
        g22 -= g21 * g12;
        g23 -= g21 * g13;
        g24 -= g21 * g14;
        h21 -= g21 * h11;
        g21 = 0;

        g32 -= g31 * g12;
        g33 -= g31 * g13;
        g34 -= g31 * g14;
        h31 -= g31 * h11;
        g31 = 0;

        g42 -= g41 * g12;
        g43 -= g41 * g13;
        g44 -= g41 * g14;
        h41 -= g41 * h11;
        g41 = 0;

        // divide second row with g22
        g23 /= g22;
        g24 /= g22;
        h21 /= g22;
        h22 /= g22;
        g22 = 1;

        // make rest of second column go to zero
        g33 -= g32 * g23;
        g34 -= g32 * g24;
        h31 -= g32 * h21;
        h32 -= g32 * h22;
        g32 = 0;

        g43 -= g42 * g23;
        g44 -= g42 * g24;
        h41 -= g42 * h21;
        h42 -= g42 * h22;
        g42 = 0;

        // divide 3rd row with g33
        g34 /= g33;
        h31 /= g33;
        h32 /= g33;
        h33 /= g33;
        g33 = 1;

        // make rest of 3rd column go to zero
        g44 -= g43 * g34;
        h41 -= g43 * h31;
        h42 -= g43 * h32;
        h43 -= g43 * h33;
        g43 = 0;

        // divide 4th row with g44
        h41 /= g44;
        h42 /= g44;
        h43 /= g44;
        h44 /= g44;
        g44 = 1;

        // second phase: going up

        // make rest of 4th column go to zero
        h31 -= g34 * h41;
        h32 -= g34 * h42;
        h33 -= g34 * h43;
        h34 -= g34 * h44;
        g34 = 0;

        h21 -= g24 * h41;
        h22 -= g24 * h42;
        h23 -= g24 * h43;
        h24 -= g24 * h44;
        g24 = 0;

        h11 -= g14 * h41;
        h12 -= g14 * h42;
        h13 -= g14 * h43;
        h14 -= g14 * h44;
        g14 = 0;

        // make rest of 3rd column go to zero
        h21 -= g23 * h31;
        h22 -= g23 * h32;
        h23 -= g23 * h33;
        h24 -= g23 * h34;
        g23 = 0;

        h11 -= g13 * h31;
        h12 -= g13 * h32;
        h13 -= g13 * h33;
        h14 -= g13 * h34;
        g13 = 0;

        // make rest of 2nd column go to zero
        h11 -= g12 * h21;
        h12 -= g12 * h22;
        h13 -= g12 * h23;
        h14 -= g12 * h24;
        g12 = 0;

        return Matrix.of(h11, h12, h13, h14,
                h21, h22, h23, h24,
                h31, h32, h33, h34,
                h41, h42, h43, h44);
    }

    public static Matrix diag(float f) {
        return new Matrix(f);
    }

    public static Matrix translate(float x, float y, float z) {
        return new Matrix(
                1, 0, 0, x,
                0, 1, 0, y,
                0, 0, 1, z,
                0, 0, 0, 1
        );
    }

    public static Matrix rotateX(float angle) {
        // x stationary, +y rotates towards +z, +z rotates towards -y
        float s = sin(angle);
        float c = cos(angle);
        return new Matrix(
                1, 0, 0, 0,
                0, c, -s, 0,
                0, s, c, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix rotateY(float angle) {
        // y stationary, +x rotates towards -z, +z rotates towards +x
        float s = sin(angle);
        float c = cos(angle);
        return new Matrix(
                c, 0, s, 0,
                0, 1, 0, 0,
                -s, 0, c, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix rotateZ(float angle) {
        // z stationary, +x rotates towards +y, +y rotates towards -x
        float s = sin(angle);
        float c = cos(angle);
        return new Matrix(
                c, -s, 0, 0,
                s, c, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        );
    }

    public static Matrix mul(Matrix a, Matrix b) {
        return new Matrix(
                a.f11 * b.f11 + a.f12 * b.f21 + a.f13 * b.f31 + a.f14 * b.f41,
                a.f11 * b.f12 + a.f12 * b.f22 + a.f13 * b.f32 + a.f14 * b.f42,
                a.f11 * b.f13 + a.f12 * b.f23 + a.f13 * b.f33 + a.f14 * b.f43,
                a.f11 * b.f14 + a.f12 * b.f24 + a.f13 * b.f34 + a.f14 * b.f44,

                a.f21 * b.f11 + a.f22 * b.f21 + a.f23 * b.f31 + a.f24 * b.f41,
                a.f21 * b.f12 + a.f22 * b.f22 + a.f23 * b.f32 + a.f24 * b.f42,
                a.f21 * b.f13 + a.f22 * b.f23 + a.f23 * b.f33 + a.f24 * b.f43,
                a.f21 * b.f14 + a.f22 * b.f24 + a.f23 * b.f34 + a.f24 * b.f44,

                a.f31 * b.f11 + a.f32 * b.f21 + a.f33 * b.f31 + a.f34 * b.f41,
                a.f31 * b.f12 + a.f32 * b.f22 + a.f33 * b.f32 + a.f34 * b.f42,
                a.f31 * b.f13 + a.f32 * b.f23 + a.f33 * b.f33 + a.f34 * b.f43,
                a.f31 * b.f14 + a.f32 * b.f24 + a.f33 * b.f34 + a.f34 * b.f44,

                a.f41 * b.f11 + a.f42 * b.f21 + a.f43 * b.f31 + a.f44 * b.f41,
                a.f41 * b.f12 + a.f42 * b.f22 + a.f43 * b.f32 + a.f44 * b.f42,
                a.f41 * b.f13 + a.f42 * b.f23 + a.f43 * b.f33 + a.f44 * b.f43,
                a.f41 * b.f14 + a.f42 * b.f24 + a.f43 * b.f34 + a.f44 * b.f44
        );
    }

    public static Matrix random(Random rng) {
        return new Matrix(
                rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat(),
                rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat(),
                rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat(),
                rng.nextFloat(), rng.nextFloat(), rng.nextFloat(), rng.nextFloat()
        );
    }

    public static Matrix of(float f11, float f12, float f13, float f14,
                            float f21, float f22, float f23, float f24,
                            float f31, float f32, float f33, float f34,
                            float f41, float f42, float f43, float f44) {
        return new Matrix(
                f11, f12, f13, f14,
                f21, f22, f23, f24,
                f31, f32, f33, f34,
                f41, f42, f43, f44
        );
    }

    public Vec transform(Vec v) {
        return Vec.of(
                f11 * v.x + f12 * v.y + f13 * v.z + f14 * v.w,
                f21 * v.x + f22 * v.y + f23 * v.z + f24 * v.w,
                f31 * v.x + f32 * v.y + f33 * v.z + f34 * v.w,
                f41 * v.x + f42 * v.y + f43 * v.z + f44 * v.w
        );
    }

    public Ray transform(Ray ray) {
        return Ray.from(transform(ray.getOrigin()), transform(ray.getDirection()));
    }

    @Override
    public String toString() {
        return String.format("[[%s %s %s %s], [%s %s %s %s], [%s %s %s %s], [%s %s %s %s]]",
                f11, f12, f13, f14, f21, f22, f23, f24, f31, f32, f33, f34, f41, f42, f43, f44);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix matrix = (Matrix) o;
        return Float.compare(matrix.f11, f11) == 0 &&
                Float.compare(matrix.f12, f12) == 0 &&
                Float.compare(matrix.f13, f13) == 0 &&
                Float.compare(matrix.f14, f14) == 0 &&
                Float.compare(matrix.f21, f21) == 0 &&
                Float.compare(matrix.f22, f22) == 0 &&
                Float.compare(matrix.f23, f23) == 0 &&
                Float.compare(matrix.f24, f24) == 0 &&
                Float.compare(matrix.f31, f31) == 0 &&
                Float.compare(matrix.f32, f32) == 0 &&
                Float.compare(matrix.f33, f33) == 0 &&
                Float.compare(matrix.f34, f34) == 0 &&
                Float.compare(matrix.f41, f41) == 0 &&
                Float.compare(matrix.f42, f42) == 0 &&
                Float.compare(matrix.f43, f43) == 0 &&
                Float.compare(matrix.f44, f44) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(f11, f12, f13, f14, f21, f22, f23, f24, f31, f32, f33, f34, f41, f42, f43, f44);
    }
}
