package rausku.material;

import org.testng.annotations.Test;
import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

import static org.testng.Assert.assertEquals;

public class CheckerBoardTextureTest {

    @Test
    public void testColor() {
        CheckerBoardTexture texture = new CheckerBoardTexture(1f, Color.of(0, 0, 0), Color.of(1, 1, 1));

        Color color = texture.getColor(new Intercept(0, Vec.point(.5f, .5f, .5f), null));

        assertEquals(color.r, 1f, 1e-6f);
        assertEquals(color.g, 1f, 1e-6f);
        assertEquals(color.b, 1f, 1e-6f);
    }
}