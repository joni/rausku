package rausku;

import rausku.math.Vec;

import static rausku.math.FloatMath.ceil;

public class CheckerBoard extends Material {
    public CheckerBoard(Color diffuseColor) {
        super(diffuseColor);
    }

    @Override
    public Color getDiffuseColor(Vec interceptPoint) {

        int baseValue = (ceil(interceptPoint.x) ^ ceil(interceptPoint.y) ^ ceil(interceptPoint.z)) & 1;
        float x = baseValue * .7f + .3f;

        return Color.of(x, x, x);
    }
}
