package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;
import rausku.math.Vec;

import static rausku.math.FloatMath.ceil;

public class CheckerBoardTexture implements Texture {
    private final float scale;
    private final Color[] diffuseColor;

    public CheckerBoardTexture(float scale) {
        this(scale, Color.of(.25f, .25f, .25f));
    }

    public CheckerBoardTexture(float scale, Color color) {
        this(scale, color, color.mul(.5f));
    }

    public CheckerBoardTexture(float scale, Color color1, Color color2) {
        this.scale = scale;
        this.diffuseColor = new Color[]{color1, color2};
    }

    @Override
    public Color getColor(Intercept intercept) {
        Vec interceptPoint = intercept.interceptPoint;
        int baseValue = (ceil(intercept.u / scale) ^ ceil(intercept.v / scale)) & 1;
//        int baseValue = (ceil(interceptPoint.x / scale) ^ ceil(interceptPoint.y / scale) ^ ceil(interceptPoint.z / scale)) & 1;
        return diffuseColor[baseValue];
    }
}
