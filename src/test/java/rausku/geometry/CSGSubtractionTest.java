package rausku.geometry;

import org.testng.annotations.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class CSGSubtractionTest {

    @Test
    public void cubeMinusSphere() {
        // when you subtract a sphere from a cube
        CSGSubtraction shape = new CSGSubtraction(
                new Cube(),
                QuadraticForm.createSphere(Vec.origin(), 1.4f));

        // and have a ray pass through a hole
        Intercept intercept = shape.getIntercept(
                Ray.fromOriginDirection(Vec.point(0, 10, 0), Vec.of(0, -1, 0)));

        // you find no intercept
        assertFalse(intercept.isValid());
    }

    @Test
    public void planeMinusSphere() {
        // when you subtract a sphere from a plane
        CSGSubtraction shape = new CSGSubtraction(
                new HalfSpace(Vec.of(0, 1, 0, 0)),
                QuadraticForm.createSphere(Vec.origin(), 1));

        // and have a ray pass down the middle
        Intercept intercept = shape.getIntercept(Ray.fromOriginDirection(Vec.point(0, 10, 0), Vec.of(0, -1, 0)));

        // it should intercept at the bottom
        assertEquals(intercept.intercept, 11f, 1e-6f);

        // if ray comes up from the hole
        Intercept intercept2 = shape.getIntercept(Ray.fromOriginDirection(Vec.point(0, -.5f, 0), Vec.of(0, +1, 0)));

        // it should not intercept
        assertFalse(intercept2.isValid());
    }

}