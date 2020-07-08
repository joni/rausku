package rausku.geometry;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HalfSpaceTest {

    @Test
    @Disabled
    public void testUV() {
        HalfSpace halfSpace = HalfSpace.horizontalPlane(-1);

        assertEquals(halfSpace.u, Vec.of(1f, -0f, 0f));
        assertEquals(halfSpace.v, Vec.of(-0f, 0f, 1));
    }

    @Test
    public void testAllInterceptsOutsideIn() {
        HalfSpace halfSpace = new HalfSpace(Vec.of(0, 1, 0, 0));

        float[] allIntercepts = halfSpace.getAllIntercepts(Ray.fromOriginDirection(Vec.point(0, 1, 0), Vec.of(0, -1, 0)));

        assertEquals(allIntercepts[0], 1f, 1e-6f);
        assertEquals(allIntercepts[1], Float.POSITIVE_INFINITY, 1e-6f);
    }

    @Test
    public void testAllInterceptsInsideOut() {
        HalfSpace halfSpace = new HalfSpace(Vec.of(0, 1, 0, 0));

        float[] allIntercepts = halfSpace.getAllIntercepts(Ray.fromOriginDirection(Vec.point(0, -1, 0), Vec.of(0, +1, 0)));

        assertEquals(allIntercepts[0], Float.NEGATIVE_INFINITY, 1e-6f);
        assertEquals(allIntercepts[1], 1f, 1e-6f);
    }
}