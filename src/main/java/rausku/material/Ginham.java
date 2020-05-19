package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

import static rausku.math.FloatMath.ceil;

public class Ginham extends Material {

    private final float scale;
    private final Color[] diffuseColor;

    public Ginham(float scale) {
        this(scale, Color.of(.5f, .5f, .5f));
    }

    public Ginham(float scale, Color diffuseColor) {
        this(scale, diffuseColor, diffuseColor);
    }

    public Ginham(float scale, Color diffuseColor, Color diffuseColor1) {
        this.scale = scale;
        this.diffuseColor = new Color[]{Color.of(1, 1, 1), diffuseColor, diffuseColor1, diffuseColor.mul(diffuseColor1)};
    }

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {
        int baseValue = ((ceil(interceptPoint.x / scale) & 1) << 1) | (ceil(interceptPoint.z / scale) & 1);
        return diffuseColor[baseValue];
    }
}
