package rausku.geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import rausku.lighting.Color;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolygonTest {

    @Test
    public void intersectTest2D() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 0), Vec.of(1, 0, 0), Vec.of(0, 1, 0));
        Ray r = Ray.fromOriginDirection(Vec.of(.25f, .25f, -1), Vec.of(0, 0, 1));
        assertEquals(getIntercept(polygon, r), 1, 1e-6);
    }

    @Test
    public void intersectTest2D_other_side() {
        Polygon polygon = new Polygon(Vec.point(0, 0, 0), Vec.point(1, 0, 0), Vec.point(0, 1, 0));
        Ray r = Ray.fromOriginDirection(Vec.of(.25f, .25f, +1), Vec.of(0, 0, -1));
        assertEquals(getIntercept(polygon, r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 1), Vec.of(1, -1, 1), Vec.of(0, 1, -1));

        Ray r = Ray.fromOriginDirection(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.v0, Vec.of(-1, -1, 1));
        assertEquals(getIntercept(polygon, r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D_1() {
        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));
        Ray ray = Ray.fromStartEnd(Vec.origin(), Vec.point(1, 1, 1));
        Intercept intercept = polygon.getIntercept(ray);
        assertTrue(intercept.isValid());
    }

    @Test
    public void intersectTest3D_2() {
        Polygon polygon = new Polygon(Vec.of(-1, -1, 1), Vec.of(1, 0, -1), Vec.of(-1, 1, 1));

        Ray r = Ray.fromOriginDirection(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(polygon.v0, Vec.of(-1, -1, 1));
        assertEquals(getIntercept(polygon, r), 1, 1e-6);
    }

    @Test
    public void intersectTest3D_3() {
        Polygon polygon = new Polygon(Vec.of(-.5f, -.5f, 1), Vec.of(1, 0, -1), Vec.of(0, 1, -1));

        Ray r = Ray.fromOriginDirection(Vec.of(0, 0, -1), Vec.of(0, 0, 1));

        assertEquals(getIntercept(polygon, r), 1, 1e-6);
    }

    @Test
    @Disabled
    public void colorTest() {
        Vec a = Vec.of(0, 0, -1);
        Vec b = Vec.of(10, 0, 0);
        Vec c = Vec.of(0, 2, -6);
        Polygon polygon = new Polygon(a, b, c);
        assertEquals(polygon.getColor(a), Color.of(1, 0, 0));
        assertEquals(polygon.getColor(b), Color.of(0, 1, 0));
        assertEquals(polygon.getColor(c), Color.of(0, 0, 1));
    }

    private float getIntercept(Polygon polygon, Ray r) {
        return polygon.getIntercept(r).intercept;
    }
}