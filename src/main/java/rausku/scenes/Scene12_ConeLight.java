package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.lighting.Color;
import rausku.lighting.ConeLight;
import rausku.material.CheckerBoard;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.sqrt;
import static rausku.math.FloatMath.toRadians;

public class Scene12_ConeLight extends Scene {
    {
        setCamera(new Camera(Matrix.mul(Matrix.rotateX(toRadians(-15)), Matrix.translate(0, 0, 15)),
                500, 500, toRadians(30)));

        float angle45 = toRadians(45);
        float angle60 = toRadians(60);
        Vec down = Vec.of(0, -1, 0);
        addLight(new ConeLight(Vec.point(-1f, 2f, 0f), down, angle45, angle60, Color.of(1f, 0f, 0f)));
        addLight(new ConeLight(Vec.point(+0f, 2f, sqrt(3)), down, angle45, angle60, Color.of(0f, 1f, 0f)));
        addLight(new ConeLight(Vec.point(+1f, 2f, 0f), down, angle45, angle60, Color.of(0f, 0f, 1f)));

        CheckerBoard checkerBoard = new CheckerBoard(1);
        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 1.0001f)), checkerBoard);
        addObject(new HalfSpace(Vec.of(0f, 0f, 1f, 0.5001f)), checkerBoard);
    }
}
