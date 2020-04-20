package rausku.material;

import rausku.lighting.Color;
import rausku.math.Vec;

import static rausku.math.FloatMath.ceil;

public class CheckerBoard extends Material {
    private float scale;

    public CheckerBoard(Color diffuseColor) {
        this(1);
    }

    public CheckerBoard(float scale) {
        this.scale = scale;
    }

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {

        int baseValue = (ceil(interceptPoint.x / scale) ^ ceil(interceptPoint.y / scale) ^ ceil(interceptPoint.z / scale)) & 1;
        float x = baseValue * .7f + .3f;

        return Color.of(x, x, x);
    }
}
