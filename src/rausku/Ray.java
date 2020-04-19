package rausku;

import rausku.math.Vec;

import java.util.ArrayList;
import java.util.List;

public class Ray implements Debuggable {

    private final Vec origin;
    private final Vec direction;
    private final float velocity;

    private final List<Object> debugInfo;

    public Ray(Vec origin, Vec direction) {
        this(origin, direction.normalize(), 1);
    }

    public Ray(Vec origin, Vec direction, float velocity) {
        this.origin = origin;
        this.direction = direction;
        this.velocity = velocity;

        this.debugInfo = new ArrayList<>(0);
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

    public Ray getTransmitted(Vec normal, Vec interceptPoint, float indexOfRefraction) {
        Vec refracted = normal.refracted(direction, indexOfRefraction);
        if (refracted == null) {
            // Total internal reflection
            return null;
        } else {
            return new Ray(interceptPoint, refracted);
        }
    }

    @Override
    public void addDebug(Object debugInfo) {
        this.debugInfo.add(debugInfo);
    }

    @Override
    public List<Object> getDebugInfo() {
        return debugInfo;
    }

    @Override
    public String toString() {
        return String.format("Ray{origin=%s, direction=%s, velocity=%s}", origin, direction, velocity);
    }
}
