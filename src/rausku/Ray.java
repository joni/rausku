package rausku;

import rausku.math.Vec;

public class Ray {

    private final Vec origin;
    private final Vec direction;
    private final float velocity;

    public Ray(Vec origin, Vec direction) {
        this(origin, direction.normalize(), 1);
    }

    public Ray(Vec origin, Vec direction, float velocity) {
        this.origin = origin;
        this.direction = direction;
        this.velocity = velocity;
    }

    public static Ray from(Vec origin, Vec canvasPoint) {
        return new Ray(origin, canvasPoint);
    }

    public Vec getDirection() {
        return direction;
    }

    public Vec getOrigin() {
        return origin;
    }

    public Vec apply(float t) {
        return Vec.mulAdd(t, direction, origin);
    }

    public Ray getReflected(Vec normal, Vec interceptPoint) {
        return new Ray(interceptPoint, normal.reflected(direction));
    }

    public Ray getRefracted(Vec normal, Vec interceptPoint, float indexOfRefraction) {
        return new Ray(interceptPoint, normal.refracted(direction, indexOfRefraction));
    }

    @Override
    public String toString() {
        return String.format("Ray{origin=%s, direction=%s, velocity=%s}", origin, direction, velocity);
    }
}
