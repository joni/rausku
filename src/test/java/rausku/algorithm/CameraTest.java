package rausku.algorithm;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import rausku.math.Ray;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rausku.math.FloatMath.toRadians;

public class CameraTest {

    @Test
    public void testDefaultLookAt() {
        Camera camera = Camera.createCamera(
                Vec.origin(),
                Vec.of(0, 0, 1),
                Vec.of(0, 1, 0),
                1000, 1000,
                toRadians(90));

        Ray topLeft = camera.getRayFromOriginToCanvas(0, 0);
        assertEquals(topLeft.direction.y(), topLeft.direction.z(), 1e-6);
        assertEquals(topLeft.direction.x(), topLeft.direction.z(), 1e-6);

        Ray middleRight = camera.getRayFromOriginToCanvas(1000, 500);
        assertEquals(middleRight.direction.y(), 0, 1e-6);
        assertEquals(middleRight.direction.x(), -middleRight.direction.z(), 1e-6);
    }

    @Test
    @Disabled
    public void testLookAt() {
        Camera camera = Camera.createCamera(
                Vec.origin(),
                Vec.of(0, 0, -1),
                Vec.of(0, 1, 0),
                1000, 1000,
                toRadians(90));

        Ray topLeft = camera.getRayFromOriginToCanvas(0, 0);
        assertEquals(topLeft.direction.y(), -topLeft.direction.z(), 1e-6);
        assertEquals(topLeft.direction.x(), topLeft.direction.z(), 1e-6);

        Ray middleRight = camera.getRayFromOriginToCanvas(1000, 500);
        assertEquals(middleRight.direction.y(), 0, 1e-6);
        assertEquals(middleRight.direction.x(), -middleRight.direction.z(), 1e-6);
    }
}