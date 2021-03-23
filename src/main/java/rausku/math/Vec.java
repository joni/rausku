package rausku.math;

import java.util.Objects;

import static java.lang.Float.max;
import static rausku.math.FloatMath.abs;
import static rausku.math.FloatMath.sqrt;

public class Vec {

    private static final Vec ORIGIN = Vec.point(0, 0, 0);
    public static final Vec I = Vec.of(1, 0, 0);
    public static final Vec J = Vec.of(0, 1, 0);
    public static final Vec K = Vec.of(0, 0, 1);

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    private Vec(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Vec of(float x, float y, float z) {
        return new Vec(x, y, z, 0);
    }

    public static Vec unit(float x, float y, float z) {
        float l = sqrt(x * x + y * y + z * z);
        return new Vec(x / l, y / l, z / l, 0);
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
        return Vec.of(x + v.x, y + v.y, z + v.z, w + v.w);
    }

    public Vec sub(Vec v) {
        return Vec.of(x - v.x, y - v.y, z - v.z, w - v.w);
    }

    public Vec mul(float a) {
        return Vec.of(a * x, a * y, a * z, a * w);
    }

    public Vec div(float a) {
        return Vec.of(x / a, y / a, z / a, w / a);
    }

    public static Vec cross(Vec u, Vec v) {
        return Vec.of(
                u.y * v.z - u.z * v.y,
                u.z * v.x - u.x * v.z,
                u.x * v.y - u.y * v.x
        );
    }

    /**
     * Computes a linear combination tU + V
     */
    public static Vec mulAdd(float t, Vec u, Vec v) {
        return new Vec(
                u.x * t + v.x,
                u.y * t + v.y,
                u.z * t + v.z,
                u.w * t + v.w
        );
    }

    /**
     * Computes a general linear combination sU + tV
     */
    public static Vec mulAdd(float s, Vec u, float t, Vec v) {
        return new Vec(
                u.x * s + v.x * t,
                u.y * s + v.y * t,
                u.z * s + v.z * t,
                u.w * s + v.w * t
        );
    }

    /**
     * Computes a general linear combination rU + sV + tW
     */
    public static Vec mulAdd(float r, Vec u, float s, Vec v, float t, Vec w) {
        return new Vec(
                r * u.x + s * v.x + t * w.x,
                r * u.y + s * v.y + t * w.y,
                r * u.z + s * v.z + t * w.z,
                r * u.w + s * v.w + t * w.w
        );
    }

    public float l1norm() {
        return max(abs(x), max(abs(y), abs(z)));
    }

    public float sqLen() {
        return x * x + y * y + z * z;
    }

    public static float cos(Vec v, Vec u) {
        return dot(v, u) / sqrt(v.sqLen() * u.sqLen());
    }

    public float dot(Vec v) {
        return dot(this, v);
    }

    public static float dot(Vec v, Vec u) {
        return v.x * u.x + v.y * u.y + v.z * u.z + v.w * u.w;
    }

    public float len() {
        return sqrt(sqLen());
    }

    public Vec normalize() {
        float len = sqrt(sqLen());
        return div(len);
    }

    public Vec toCanonical() {
        if (w == 0 || w == 1) {
            return this;
        } else {
            return Vec.of(x / w, y / w, z / w, 1);
        }
    }

    public Vec toVector() {
        return Vec.of(x, y, z);
    }

    public Vec toPoint() {
        return Vec.point(x, y, z);
    }

    public Vec perpendicular() {
        if (abs(x) > abs(y)) {
            if (abs(y) > abs(z)) {
                // x > y > z
                return Vec.unit(-y, x, 0);
            } else if (abs(x) > abs(z)) {
                // x > z > y
                return Vec.unit(-z, 0, x);
            } else {
                // z > x > y
                return Vec.unit(z, 0, -x);
            }
        } else {
            if (abs(z) > abs(x)) {
                // y > z > x
                return Vec.unit(0, -z, y);
            } else if (abs(x) > abs(z)) {
                // y > x > z
                return Vec.unit(y, -x, 0);
            } else {
                // z > y > x
                return Vec.unit(0, z, -y);
            }
        }
    }

    /**
     * Compute the reflection of {@code v} around this vector. In other words, the current vector is a normal vector and
     * light arrives with direction {@code v}, the reflected light leaves in the direction returned by this method.
     *
     * @param v a unit vector
     * @return reflection of v
     */
    public Vec reflected(Vec v) {
        // assert normalized
        return mulAdd(-2 * this.dot(v), this, v);
    }

    /**
     * Compute the refraction of {@code v} around this vector. In other words, the current vector is a normal vector and
     * light arrives with direction {@code v}, the light is transmitted to the other side of the surface in the
     * direction returned by this method.
     *
     * @param v a unit vector
     * @param r index of refraction
     * @return refracted direction
     */
    public Vec refracted(Vec v, float r) {
        // assert normalized
        float dot = this.dot(v);
        float s, t;
        if (dot > 0) {
            // Ray is exiting the surface
            t = r;
            s = t * dot - sqrt(1 - t * t * (1 - dot * dot));
        } else {
            // Ray is entering the surface
            t = 1 / r;
            s = t * dot + sqrt(1 - t * t * (1 - dot * dot));
        }
        if (Float.isFinite(s)) {
            return mulAdd(-s, this, t, v);
        } else {
            // Total internal reflection
            return null;
        }
    }

    @Override
    public String toString() {
        return String.format("[%.2f %.2f %.2f %.2f]", x, y, z, w);
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
