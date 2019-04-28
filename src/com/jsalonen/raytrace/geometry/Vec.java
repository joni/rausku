package com.jsalonen.raytrace.geometry;

import static java.lang.Math.*;

public class Vec {

    public float x;
    public float y;
    public float z;

    private Vec() {
        // initialze to 0
    }

    private Vec(Vec v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec mulAdd(float t, Vec direction, Vec origin) {
        return new Vec(direction.x * t + origin.x,
                direction.y * t + origin.y,
                direction.z * t + origin.z);
    }

    /**
     * Computes sU + tV
     */
    public static Vec mulAdd(float s, Vec u, float t, Vec v) {
        return new Vec(u.x * s + v.x * t,
                u.y * s + v.y * t,
                u.z * s + v.z * t);
    }

    public static Vec of(float x, float y, float z) {
        return new Vec(x, y, z);
    }

    public static Vec of(double x, double y, double z) {
        return new Vec(((float) x), (float) y, (float) z);
    }

    public Vec add(Vec v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vec sub(Vec v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vec mul(float a) {
        x *= a;
        y *= a;
        z *= a;
        return this;
    }

    public Vec div(float a) {
        x /= a;
        y /= a;
        z /= a;
        return this;
    }

    public float l1norm() {
        return max(abs(x), max(abs(y), abs(z)));
    }

    public float sqLen() {
        return x * x + y * y + z * z;
    }

    public float dot(Vec v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec normalize() {
        float len = (float) sqrt(sqLen());
        return div(len);
    }

    public Vec copy() {
        return new Vec(this);
    }

    @Override
    public String toString() {
        return String.format("Vec(%f, %f, %f)", x, y, z);
    }

    public Vec reflected(Vec v) {
        // assert normalized
        return mulAdd(-2 * this.dot(v), this, v);
    }

    public float cos(Vec v) {
        return this.dot(v) / (float) sqrt(this.sqLen() * v.sqLen());
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
}
