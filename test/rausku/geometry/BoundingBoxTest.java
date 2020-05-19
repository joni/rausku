package rausku.geometry;

import org.testng.annotations.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

import static org.testng.Assert.*;

public class BoundingBoxTest {

    @Test
    public void testOnePoint() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        builder.addPoint(Vec.point(1, 2, -3));

        BoundingBox build = builder.build();

        assertEquals(build.minX, 1f, 1e-6);
        assertEquals(build.maxX, 1f, 1e-6);
        assertEquals(build.minY, 2f, 1e-6);
        assertEquals(build.maxY, 2f, 1e-6);
        assertEquals(build.minZ, -3f, 1e-6);
        assertEquals(build.maxZ, -3f, 1e-6);
    }

    @Test
    public void testOnePolygon() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        Polygon polygon = new Polygon(Vec.point(1, 2, -3), Vec.point(2, -3, 1), Vec.point(-3, 1, 2));

        builder.addPolygons(List.of(polygon));

        BoundingBox bbox = builder.build();

        assertEquals(bbox.minX, -3f, 1e-6);
        assertEquals(bbox.maxX, 2f, 1e-6);
        assertEquals(bbox.minY, -3f, 1e-6);
        assertEquals(bbox.maxY, 2f, 1e-6);
        assertEquals(bbox.minZ, -3f, 1e-6);
        assertEquals(bbox.maxZ, 2f, 1e-6);
    }

    @Test
    public void testInterceptWithAxisAlignedRay() {
        BoundingBox bbox = new BoundingBox(Vec.origin(), Vec.point(2, 2, 2));

        Ray ray = Ray.fromOriginDirection(Vec.point(1, 1, 3), Vec.of(0, 0, -1));

        float[] intercepts = bbox.getIntercepts(ray);
        assertEquals(intercepts[0], 1, 1e-6);
        assertEquals(intercepts[1], 3, 1e-6);
    }

    @Test
    public void testInterceptWhenRayPassesBy() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));

        builder.addPolygons(List.of(polygon));

        BoundingBox bbox = builder.build();

        Ray ray = Ray.fromOriginDirection(Vec.point(2, 0, 2), Vec.of(0, 1, 0));

        assertFalse(bbox.testIntercept(ray));
    }

    @Test
    public void testInterceptWhenRayGoesAway() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));

        builder.addPolygons(List.of(polygon));

        BoundingBox bbox = builder.build();

        Ray ray = Ray.fromOriginDirection(Vec.point(2, 2, 2), Vec.of(1, 1, 1));

        assertFalse(bbox.testIntercept(ray));
    }

    @Test
    public void testInterceptWhenRayStartsFromInside() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));

        builder.addPolygons(List.of(polygon));

        BoundingBox bbox = builder.build();

        Ray ray = Ray.fromOriginDirection(Vec.point(.5f, .5f, .5f), Vec.of(1, 1, 1));

        assertTrue(bbox.testIntercept(ray));
    }

    @Test
    public void testIntercept() {
        BoundingBox.Builder builder = new BoundingBox.Builder();

        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));

        builder.addPolygons(List.of(polygon));

        BoundingBox bbox = builder.build();

        Ray ray = Ray.fromStartEnd(Vec.point(2, 2, 2), Vec.origin());

        assertTrue(bbox.testIntercept(ray));
    }
}