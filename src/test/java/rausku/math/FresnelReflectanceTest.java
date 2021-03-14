package rausku.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rausku.math.FloatMath.sqrt;

public class FresnelReflectanceTest {

    @Test
    public void testRightAngleEnter() {
        float reflectance = FresnelReflectance.dielectric(1, 1, 1, 2);
        assertEquals(1 / 9f, reflectance, 1e-6);
    }

    @Test
    public void testGlancingAngle() {
        float reflectance = FresnelReflectance.dielectric(0, 2, sqrt(.5f), 1);
        assertEquals(1f, reflectance, 1e-6);
    }
}