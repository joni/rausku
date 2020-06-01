package rausku.math;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class PerlinNoiseTest {

    @Test
    public void testCoeff() {
        PerlinNoise2D noise = new PerlinNoise2D();
        assertEquals(noise.getGradient(.125f, .75f, 0), .125f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 1), .875f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 2), .750f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 3), .625f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 4), -.125f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 5), -.875f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 6), -.75f, 1e-6f);
        assertEquals(noise.getGradient(.125f, .75f, 7), -.625f, 1e-6f);
    }
}