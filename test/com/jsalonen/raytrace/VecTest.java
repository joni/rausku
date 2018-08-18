package com.jsalonen.raytrace;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.lang.Math.*;
import static org.testng.Assert.assertEquals;

public class VecTest {

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void testRefractedEnter() {
        for (int i = 0; i <= 90; i++) {
            double incidentAngle = toRadians(i);
            Vec incident = Vec.of((float) sin(incidentAngle), (float) -cos(incidentAngle), 0);
            Vec normal = Vec.of(0, 1, 0);
            Vec refracted = normal.refracted(incident, 1.5f);
            double expected = asin(sin(incidentAngle) / 1.5);
            double actual = acos(-refracted.cos(normal));
            assertEquals(toDegrees(actual), toDegrees(expected), 1e-3, String.valueOf(i));
        }
    }

    @Test
    public void testRefractedExit() {
        for (int i = 0; i < 90; i++) {
            double incidentAngle = toRadians(i);
            Vec incident = Vec.of((float) sin(incidentAngle), (float) cos(incidentAngle), 0);
            Vec normal = Vec.of(0, 1, 0);
            Vec refracted = normal.refracted(incident, 1.5f);
            double expected = asin(sin(incidentAngle) * 1.5);
            double actual = acos(refracted.cos(normal));
            assertEquals(toDegrees(actual), toDegrees(expected), 1e-3);
        }
    }

    @Test
    public void testTotalInternalReflection() {
        double incidentAngle = toRadians(80);
        Vec incident = Vec.of((float) sin(incidentAngle), (float) cos(incidentAngle), 0);
        Vec normal = Vec.of(0, 1, 0);
        Vec refracted = normal.refracted(incident, 1.5f);
        assertEquals(refracted.sqLen(), Double.NaN, 1e-3);
    }
}