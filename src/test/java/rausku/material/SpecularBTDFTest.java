package rausku.material;

import org.junit.jupiter.api.Test;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rausku.math.FloatMath.sqrt;

public class SpecularBTDFTest {

    @Test
    void testSampleReflected() {
        SpecularBTDF specularBTDF = new SpecularBTDF(2);
        BRDF.Sample sample = specularBTDF.sample(Vec.unit(+1, -1, 0), 0, 0);
        assertEquals(Vec.unit(+1, +1, 0), sample.incident);
    }

    @Test
    void testSampleRefracted() {
        SpecularBTDF specularBTDF = new SpecularBTDF(2);
        BRDF.Sample sample = specularBTDF.sample(Vec.unit(+1, -1, 0), 1, 0);
        assertEquals(Vec.unit(+1f, -sqrt(7f), 0), sample.incident);
    }
}