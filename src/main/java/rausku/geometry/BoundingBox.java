package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class BoundingBox {

    private static final float[] NO_INTERCEPT = {};

    private static final BoundingBox unbounded = new BoundingBox(
            Vec.point(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY),
            Vec.point(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    );

    public final float minX;
    public final float maxX;
    public final float minY;
    public final float maxY;
    public final float minZ;
    public final float maxZ;

    private BoundingBox(Builder builder) {
        this.minX = builder.minX;
        this.maxX = builder.maxX;
        this.minY = builder.minY;
        this.maxY = builder.maxY;
        this.minZ = builder.minZ;
        this.maxZ = builder.maxZ;
    }

    public BoundingBox(Vec minPoint, Vec maxPoint) {
        this.minX = minPoint.x;
        this.minY = minPoint.y;
        this.minZ = minPoint.z;
        this.maxX = maxPoint.x;
        this.maxY = maxPoint.y;
        this.maxZ = maxPoint.z;
    }

    public static BoundingBox unbounded() {
        return unbounded;
    }

    public boolean testIntercept(Ray ray) {
        Vec rayOrigin = ray.origin;
        Vec rayDirection = ray.direction;

        Range xRange = new Range(minX, maxX, rayOrigin.x, rayDirection.x);
        Range yRange = new Range(minY, maxY, rayOrigin.y, rayDirection.y);
        Range zRange = new Range(minZ, maxZ, rayOrigin.z, rayDirection.z);

        float max = Math.min(Math.min(xRange.max, yRange.max), zRange.max);
        float min = Math.max(Math.max(xRange.min, yRange.min), zRange.min);

        return 0 < max && min <= max;
    }

    public float[] getIntercepts(Ray ray) {
        Vec rayOrigin = ray.origin;
        Vec rayDirection = ray.direction;

        Range xRange = new Range(minX, maxX, rayOrigin.x, rayDirection.x);
        Range yRange = new Range(minY, maxY, rayOrigin.y, rayDirection.y);
        Range zRange = new Range(minZ, maxZ, rayOrigin.z, rayDirection.z);

        float max = Math.min(Math.min(xRange.max, yRange.max), zRange.max);
        float min = Math.max(Math.max(xRange.min, yRange.min), zRange.min);

        if (0 < max && min <= max) {
            return new float[]{Math.max(0, min), max};
        } else {
            return NO_INTERCEPT;
        }
    }

    public boolean contains(Vec point) {
        return minX <= point.x && point.x < maxX
                && minY <= point.y && point.y < maxY
                && minZ <= point.z && point.z < maxZ;
    }

    private static class Range {

        final float min;
        final float max;

        Range(float min, float max, float o, float d) {
            if (d >= 0) {
                this.min = (min - o) / d;
                this.max = (max - o) / d;
            } else {
                this.min = (max - o) / d;
                this.max = (min - o) / d;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("BoundingBox{%s, %s, %s}{%s, %s, %s}", minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static class Builder {

        private float minX = Float.POSITIVE_INFINITY;
        private float maxX = Float.NEGATIVE_INFINITY;
        private float minY = Float.POSITIVE_INFINITY;
        private float maxY = Float.NEGATIVE_INFINITY;
        private float minZ = Float.POSITIVE_INFINITY;
        private float maxZ = Float.NEGATIVE_INFINITY;

        public Builder addPolygons(List<Polygon> polygons) {
            for (Polygon polygon : polygons) {
                addPoint(polygon.v0);
                addPoint(polygon.v1);
                addPoint(polygon.v2);
            }
            return this;
        }

        public Builder addPoint(Vec point) {
            minX = Math.min(minX, point.x);
            maxX = Math.max(maxX, point.x);
            minY = Math.min(minY, point.y);
            maxY = Math.max(maxY, point.y);
            minZ = Math.min(minZ, point.z);
            maxZ = Math.max(maxZ, point.z);
            return this;
        }

        public BoundingBox build() {
            return new BoundingBox(this);
        }
    }
}
