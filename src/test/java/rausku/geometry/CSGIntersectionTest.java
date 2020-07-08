package rausku.geometry;

import org.junit.jupiter.api.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSGIntersectionTest {

    @Test
    public void cubeIntersectSphere() {
        // when you Intersect a sphere and cube
        CSGIntersection shape = new CSGIntersection(
                new Cube(),
                QuadraticForm.createSphere(Vec.origin(), 1.4f));

        // and have a ray pass through the middle
        Intercept intercept = shape.getIntercept(
                Ray.fromOriginDirection(Vec.point(0, 10, 0), Vec.of(0, -1, 0)));

        // it intersects at the cube
        assertEquals(intercept.intercept, 9, 1e-6);
    }

    @Test
    public void planeIntersectSphere() {
        // when you Intersect a sphere from a plane
        CSGIntersection shape = new CSGIntersection(
                new HalfSpace(Vec.of(0, 1, 0, 0)),
                QuadraticForm.createSphere(Vec.origin(), 1));

        // and have a ray pass down the middle
        Intercept intercept = shape.getIntercept(Ray.fromOriginDirection(Vec.point(0, 10f, 0), Vec.of(0, -1, 0)));

        // it should intercept at the plane
        assertEquals(intercept.intercept, 10f, 1e-6f);

        // if ray comes up from the other side
        Intercept intercept2 = shape.getIntercept(Ray.fromOriginDirection(Vec.point(0, -10f, 0), Vec.of(0, +1, 0)));

        // it should intercept at the sphere
        assertEquals(intercept2.intercept, 9f, 1e-6f);
    }

}