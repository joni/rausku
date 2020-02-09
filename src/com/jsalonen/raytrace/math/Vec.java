package com.jsalonen.raytrace.math;

import java.util.Objects;

import static com.jsalonen.raytrace.math.FloatMath.*;
import static java.lang.Float.max;

public class Vec {

    private static final Vec ORIGIN = Vec.point(0, 0, 0);

    public float x;
    public float y;
    public float z;
    public float w;

    private Vec() {
        x = y = z = w = 0;
    }

    private Vec(Vec v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = v.w;
    }

    public Vec(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vec mulAdd(float t, Vec direction, Vec origin) {
        return new Vec(direction.x * t + origin.x,
                direction.y * t + origin.y,
                direction.z * t + origin.z,
                direction.w * t + origin.w);
    }

    /**
     * Computes sU + tV
     */
    public static Vec mulAdd(float s, Vec u, float t, Vec v) {
        return new Vec(u.x * s + v.x * t,
                u.y * s + v.y * t,
                u.z * s + v.z * t,
                u.w * s + v.w * t);
    }

    public static Vec of(float x, float y, float z) {
        return new Vec(x, y, z, 0);
    }

    public static Vec of(float x, float y, float z, float w) {
        return new Vec(x, y, z, w);
    }

    public static Vec point(float x, float y, float z) {
        return new Vec(x, y, z, 1);
    }

    public static Vec origin() {
        return ORIGIN;
    }

    public Vec add(Vec v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    public Vec sub(Vec v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    public Vec mul(float a) {
        x *= a;
        y *= a;
        z *= a;
        w *= a;
        return this;
    }

    public Vec div(float a) {
        x /= a;
        y /= a;
        z /= a;
        w /= a;
        return this;
    }

    public float l1norm() {
        return max(abs(x), max(abs(y), abs(z)));
    }

    public float sqLen() {
        return x * x + y * y + z * z;
    }

    public double len() {
        return Math.sqrt(sqLen());
    }

    public float dot(Vec v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec normalize() {
        float len = sqrt(sqLen());
        return div(len);
    }

    public Vec copy() {
        return new Vec(this);
    }

    @Override
    public String toString() {
        return String.format("[%f %f %f %f]", x, y, z, w);
    }

    public Vec reflected(Vec v) {
        // assert normalized
        return mulAdd(-2 * this.dot(v), this, v);
    }

    public float cos(Vec v) {
        return this.dot(v) / sqrt(this.sqLen() * v.sqLen());
    }

    public Vec refracted(Vec v, float r) {
        // assert normalized
        float dot = this.dot(v);
        float s, t;
        if (dot > 0) {
            t = r;
            s = (float) (t * dot - sqrt(1 - pow(t, 2) * (1 - pow(dot, 2))));
        } else {
            t = 1 / r;
            s = (float) (t * dot + sqrt(1 - pow(t, 2) * (1 - pow(dot, 2))));
        }
        return mulAdd(-s, this, t, v);
    }

    public static Vec cross(Vec v, Vec w) {
        return Vec.of(v.y * w.z - v.z * w.y, v.z * w.x - v.x * w.z, v.x * w.y - v.y * w.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec vec = (Vec) o;
        return Float.compare(vec.x, x) == 0 &&
                Float.compare(vec.y, y) == 0 &&
                Float.compare(vec.z, z) == 0 &&
                Float.compare(vec.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
