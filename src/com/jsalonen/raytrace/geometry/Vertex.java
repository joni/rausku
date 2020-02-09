package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.math.Vec;

public class Vertex {
    public final Vec position;
    public final Vec normal;

    private Vertex(Vec position, Vec normal) {
        this.position = position;
        this.normal = normal;
    }

    public static Vertex of(Vec position, Vec normal) {
        return new Vertex(position, normal);
    }
}
