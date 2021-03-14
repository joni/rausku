package rausku.material;

import org.junit.jupiter.api.Test;
import rausku.lighting.Color;
import rausku.math.Vec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rausku.math.Vec.unit;

public class SpecularBRDFTest {

    @Test
    void testIncidentDirection() {
        SpecularBRDF specularBRDF = new SpecularBRDF(Color.of(1));
        BRDF.Sample sample = specularBRDF.sample(unit(+1, -1, 0), 0, 0);
        assertEquals(Vec.unit(+1, +1, 0), sample.incident);
    }
}