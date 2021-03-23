package rausku.math;

import rausku.Debuggable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Ray implements Debuggable {

    public final Vec origin;
    public final Vec direction;
    public final float bound;

    private final List<Object> debugInfo;

    private Ray(Vec origin, Vec direction) {
        this(origin, direction, Float.POSITIVE_INFINITY);
    }

    private Ray(Vec origin, Vec direction, float bound) {
        this.origin = origin;
        this.direction = direction;
        this.bound = bound;

        this.debugInfo = new ArrayList<>(0);
    }

    public static Ray fromOriginDirection(Vec origin, Vec direction) {
        return new Ray(origin, direction);
    }

    public static Ray fromStartEnd(Vec startPoint, Vec endPoint) {
        Vec direction = endPoint.sub(startPoint);
        return new Ray(startPoint, direction.normalize(), direction.len());
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
        return String.format("Ray{origin=%s, direction=%s, bound=%s}", origin, direction, bound);
    }

    public Ray jitterDirection() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        Vec rndVec = Vec.of(rnd.nextFloat() - .5f, rnd.nextFloat() - .5f, rnd.nextFloat() - .5f).mul(.1f);
        return new Ray(origin, direction.add(rndVec).normalize(), bound);
    }

    public Ray withOrigin(Vec origin) {
        return new Ray(origin, this.direction, this.bound);
    }
}
