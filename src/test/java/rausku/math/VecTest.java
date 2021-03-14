package rausku.math;


import org.junit.jupiter.api.Test;

import java.util.Random;

import static java.lang.Math.toDegrees;
import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static rausku.math.FloatMath.cos;
import static rausku.math.FloatMath.sin;
import static rausku.math.FloatMath.toRadians;

public class VecTest {

    @Test
    public void testAdd() {
        Vec u = Vec.of(1, 2, -3);
        Vec v = Vec.of(2, -3, 1);
        assertEquals(Vec.of(3, -1, -2), u.add(v));
    }

    @Test
    public void testSub() {
        Vec u = Vec.of(1, 2, -3);
        Vec v = Vec.of(2, -3, 1);
        assertEquals(Vec.of(-1, 5, -4), u.sub(v));
    }

    @Test
    public void testDotProduct() {
        Vec v = Vec.of(1, 2, 3);
        Vec u = Vec.of(-3, 2, 1);
        assertEquals(4f, Vec.dot(v, u), 1e-5f);
    }

    @Test
    public void testCanonical() {
        Vec v = Vec.of(6, 4, 2, 2);
        Vec u = v.toCanonical();
        assertEquals(Vec.point(3, 2, 1), u);
    }

    @Test
    public void testCrossProduct() {
        assertEquals(Vec.K, Vec.cross(Vec.I, Vec.J));
        assertEquals(Vec.I, Vec.cross(Vec.J, Vec.K));
        assertEquals(Vec.J, Vec.cross(Vec.K, Vec.I));

        assertEquals(Vec.of(0, 0, -1), Vec.cross(Vec.J, Vec.I));
        assertEquals(Vec.of(-1, 0, 0), Vec.cross(Vec.K, Vec.J));
        assertEquals(Vec.of(0, -1, 0), Vec.cross(Vec.I, Vec.K));
    }

    @Test
    void testMulAdd() {
        assertEquals(Vec.of(1, 2, 3), Vec.mulAdd(1, Vec.I, 2, Vec.J, 3, Vec.K));
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

    @Test
    public void testPerpendicular() {
        Random rnd = new Random();
        for (int i = 0; i < 100; i++) {
            Vec v = Vec.of(((float) rnd.nextGaussian()), (float) rnd.nextGaussian(), (float) rnd.nextGaussian());
            Vec perp = v.perpendicular();
            assertEquals(v.dot(perp), 0, 1e-6);
        }
    }
}