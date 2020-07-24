package rausku.geometry;

import org.junit.jupiter.api.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.*;

public class TorusTest {

    @Test
    public void testBoundingBox() {
        Torus torus = new Torus(2, 1);
        BoundingBox boundingBox = torus.getBoundingBox();

        assertEquals(boundingBox.minX, -3, 1e-6f);
        assertEquals(boundingBox.minZ, -3, 1e-6f);
        assertEquals(boundingBox.minY, -1, 1e-6f);
        assertEquals(boundingBox.maxX, 3, 1e-6f);
        assertEquals(boundingBox.maxZ, 3, 1e-6f);
        assertEquals(boundingBox.maxY, 1, 1e-6f);
    }

    @Test
    public void testRayMiss() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromOriginDirection(Vec.point(2.5f, 2f, 2.5f), Vec.of(0, -1, 0));
        Intercept intercept = torus.getIntercept(ray);
        assertFalse(intercept.isValid());
    }

    @Test
    public void testRayMissCenter() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromStartEnd(Vec.point(0, 3, 0), Vec.origin());
        Intercept intercept = torus.getIntercept(ray);
        assertFalse(intercept.isValid());
    }

    @Test
    public void testRayHit() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromOriginDirection(Vec.point(4, 0, 0), Vec.of(-1, 0, 0));
        Intercept intercept = torus.getIntercept(ray);
        assertEquals(intercept.intercept, 1, 1e-6f);
    }

    @Test
    public void testRayHitCenter() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromOriginDirection(Vec.point(.5f, 0, 0), Vec.point(-1.5f, 0, 0));
        Intercept intercept = torus.getIntercept(ray);
        assertEquals(intercept.intercept, 1f, 1e-6f);
    }

    @Test
    public void testRayHitVertical() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromOriginDirection(Vec.point(2, 2, 0), Vec.point(0, -1, 0));
        Intercept intercept = torus.getIntercept(ray);
        assertEquals(intercept.intercept, 1, 1e-6f);
    }

    @Test
    public void testNormal() {
        Torus torus = new Torus(2, 1);
        Ray ray = Ray.fromOriginDirection(Vec.point(4, 0, 0), Vec.of(-1, 0, 0));
        Intercept intercept = torus.getIntercept(ray);
        Vec normal = torus.getNormal(intercept);
        assertNotNull(normal);
        assertEquals(normal.x, 1, 1e-6);
        assertEquals(normal.y, 0, 1e-6);
        assertEquals(normal.z, 0, 1e-6);
    }
}