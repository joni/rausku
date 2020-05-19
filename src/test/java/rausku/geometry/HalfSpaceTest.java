package rausku.geometry;

import org.testng.annotations.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.testng.Assert.assertEquals;

public class HalfSpaceTest {

    @Test
    public void testAllInterceptsOutsideIn() {
        HalfSpace halfSpace = new HalfSpace(Vec.of(0, 1, 0, 0), null);

        float[] allIntercepts = halfSpace.getAllIntercepts(Ray.fromOriginDirection(Vec.point(0, 1, 0), Vec.of(0, -1, 0)));

        assertEquals(allIntercepts[0], 1f, 1e-6f);
        assertEquals(allIntercepts[1], Float.POSITIVE_INFINITY, 1e-6f);
    }

    @Test
    public void testAllInterceptsInsideOut() {
        HalfSpace halfSpace = new HalfSpace(Vec.of(0, 1, 0, 0), null);

        float[] allIntercepts = halfSpace.getAllIntercepts(Ray.fromOriginDirection(Vec.point(0, -1, 0), Vec.of(0, +1, 0)));

        assertEquals(allIntercepts[0], Float.NEGATIVE_INFINITY, 1e-6f);
        assertEquals(allIntercepts[1], 1f, 1e-6f);
    }
}