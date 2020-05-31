package rausku.material;

import org.testng.annotations.Test;
import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

import static org.testng.Assert.assertEquals;

public class GinghamTextureTest {

    @Test
    public void testColor() {
        Color magenta = Color.of(1, 0, 1);
        Color cyan = Color.of(0, 1, 1);
        Color blue = Color.of(0, 0, 1);
        Color white = Color.of(1, 1, 1);
        GinghamTexture texture = new GinghamTexture(1f, magenta, cyan);

        Color color1 = texture.getColor(new Intercept(0, Vec.origin(), .5f, .5f, null));
        assertEquals(color1, blue);
        Color color2 = texture.getColor(new Intercept(0, Vec.origin(), .5f, 1.5f, null));
        assertEquals(color2, cyan);
        Color color3 = texture.getColor(new Intercept(0, Vec.origin(), 1.5f, .5f, null));
        assertEquals(color3, magenta);
        Color color4 = texture.getColor(new Intercept(0, Vec.origin(), 1.5f, 1.5f, null));
        assertEquals(color4, white);
    }
}