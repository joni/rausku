package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;

import static java.lang.Math.sin;

public class BumpySphere extends Sphere {
    public BumpySphere(Vec center, float radius, Material material) {
        super(center, radius, material);
    }

    @Override
    public Vec getNormal(Ray ray, Vec point) {
        Vec bump = Vec.of(sin(point.x * 50), sin(point.y * 50), sin(point.z * 50)).div(50);
        return super.getNormal(ray, point).add(bump);
    }
}
