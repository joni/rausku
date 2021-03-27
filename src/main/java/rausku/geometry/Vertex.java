package rausku.geometry;

import rausku.math.Vec;

public record Vertex(Vec position, Vec normal) {

    public static Vertex of(Vec position, Vec normal) {
        return new Vertex(position, normal);
    }
}
