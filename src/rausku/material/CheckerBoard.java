package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

import static rausku.math.FloatMath.ceil;

public class CheckerBoard extends Material {

    private final float scale;
    private final Color[] diffuseColor;

    public CheckerBoard(float scale) {
        this(scale, Color.of(.25f, .25f, .25f));
    }

    public CheckerBoard(float scale, Color diffuseColor) {
        this.scale = scale;
        this.diffuseColor = new Color[]{Color.of(1, 1, 1), diffuseColor};
    }

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {

        int baseValue = (ceil(interceptPoint.x / scale) ^ ceil(interceptPoint.y / scale) ^ ceil(interceptPoint.z / scale)) & 1;

        return diffuseColor[baseValue];

        return Color.of(x, x, x);
    }
}
