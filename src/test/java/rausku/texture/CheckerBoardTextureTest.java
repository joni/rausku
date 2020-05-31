package rausku.texture;

import org.testng.annotations.Test;
import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

import static org.testng.Assert.assertEquals;

public class CheckerBoardTextureTest {

    @Test
    public void testColor() {
        Color w = Color.of(1, 1, 1);
        Color b = Color.of(0, 0, 0);
        CheckerBoardTexture texture = new CheckerBoardTexture(1f, w, b);

        Color color = texture.getColor(new Intercept(0, Vec.origin(), .5f, .5f, null));
        assertEquals(color, w);
        Color color2 = texture.getColor(new Intercept(0, Vec.origin(), 1.5f, .5f, null));
        assertEquals(color2, b);
    }
}