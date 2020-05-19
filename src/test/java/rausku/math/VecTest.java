package rausku.math;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.lang.Math.toDegrees;
import static java.lang.Math.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static rausku.math.FloatMath.cos;
import static rausku.math.FloatMath.sin;
import static rausku.math.FloatMath.toRadians;

public class VecTest {

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void testAdd() {
        Vec u = Vec.of(1, 2, -3);
        Vec v = Vec.of(2, -3, 1);
        assertEquals(u.add(v), Vec.of(3, -1, -2));
    }

    @Test
    public void testSub() {
        Vec u = Vec.of(1, 2, -3);
        Vec v = Vec.of(2, -3, 1);
        assertEquals(u.sub(v), Vec.of(-1, 5, -4));
    }

    @Test
    public void testDotProduct() {
        Vec v = Vec.of(1, 2, 3);
        Vec u = Vec.of(-3, 2, 1);
        assertEquals(Vec.dot(v, u), 4f, 1e-5f);
    }

    @Test
    public void testCanonical() {
        Vec v = Vec.of(6, 4, 2, 2);
        Vec u = v.toCanonical();
        assertEquals(u, Vec.point(3, 2, 1));
    }

    @Test
    public void testCrossProduct() {
        Vec i = Vec.of(1, 0, 0);
        Vec j = Vec.of(0, 1, 0);
        Vec k = Vec.of(0, 0, 1);

        assertEquals(k, Vec.cross(i, j));
        assertEquals(i, Vec.cross(j, k));
        assertEquals(j, Vec.cross(k, i));

        assertEquals(Vec.of(0, 0, -1), Vec.cross(j, i));
        assertEquals(Vec.of(-1, 0, 0), Vec.cross(k, j));
        assertEquals(Vec.of(0, -1, 0), Vec.cross(i, k));
    }

    @Test
    public void testRefractedEnter() {
        for (int i = 0; i < 90; i++) {
            float incidentAngle = toRadians(i);
            Vec incident = Vec.of(sin(incidentAngle), -cos(incidentAngle), 0);
            Vec normal = Vec.of(0, 1, 0);
            Vec refracted = normal.refracted(incident, 1.5f);
            double expected = asin(sin(incidentAngle) / 1.5);
            double actual = acos(-Vec.cos(refracted, normal));
            assertEquals(toDegrees(actual), toDegrees(expected), 1e-3, String.valueOf(i));
        }
    }

    @Test
    public void testRefractedExit() {
        for (int i = 0; i < 42; i++) {
            float incidentAngle = toRadians(i);
            Vec incident = Vec.of(sin(incidentAngle), cos(incidentAngle), 0);
            Vec normal = Vec.of(0, 1, 0);
            Vec refracted = normal.refracted(incident, 1.5f);
            double expected = asin(sin(incidentAngle) * 1.5);
            double actual = acos(Vec.cos(refracted, normal));
            assertEquals(toDegrees(actual), toDegrees(expected), 1e-3);
        }
    }

    @Test
    public void testTotalInternalReflection() {
        float incidentAngle = toRadians(80);
        Vec incident = Vec.of(sin(incidentAngle), cos(incidentAngle), 0);
        Vec normal = Vec.of(0, 1, 0);
        Vec refracted = normal.refracted(incident, 1.5f);
        assertNull(refracted);
    }
}