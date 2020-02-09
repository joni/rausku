package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.math.Vec;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PolygonTest {

    @Test
    public void intersectTest2D() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 0), Vec.of(1, 0, 0), Vec.of(0, 1, 0));

        Ray r = new Ray(Vec.of(.25f, .25f, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.v0, Vec.of(-1, -1, 0));
        assertEquals(polygon.v1, Vec.of(2f, 1f, 0));
        assertEquals(polygon.v2, Vec.of(1f, 2f, 0));
        assertEquals(polygon.getIntercept(r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 1), Vec.of(1, -1, 1), Vec.of(0, 1, -1));

        Ray r = new Ray(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.v0, Vec.of(-1, -1, 1));
        assertEquals(polygon.v1, Vec.of(2f, 0f, 0f));
        assertEquals(polygon.v2, Vec.of(1f, 2f, -2f));
        assertEquals(polygon.getIntercept(r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D_2() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 1), Vec.of(1, 0, -1), Vec.of(-1, 1, 1));

        Ray r = new Ray(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.v0, Vec.of(-1, -1, 1));
        assertEquals(polygon.v1, Vec.of(2f, 1f, -2f));
        assertEquals(polygon.v2, Vec.of(0f, 2f, 0f));
        assertEquals(polygon.getIntercept(r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D_3() {
        Polygon polygon = new Polygon(Vec.of(-.5f, -.5f, 1), Vec.of(1, 0, -1), Vec.of(0, 1, -1));

        Ray r = new Ray(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.getIntercept(r), 1, 1e-6);
    }

    @Test
    public void colorTest() {
        Vec a = Vec.of(0, 0, -1);
        Vec b = Vec.of(10, 0, 0);
        Vec c = Vec.of(0, 2, -6);
        Polygon polygon = new Polygon(a, b, c);
        assertEquals(polygon.getColor(a), Color.of(1, 0, 0));
        assertEquals(polygon.getColor(b), Color.of(0, 1, 0));
        assertEquals(polygon.getColor(c), Color.of(0, 0, 1));
    }
}