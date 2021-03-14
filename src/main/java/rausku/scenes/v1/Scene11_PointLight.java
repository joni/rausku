package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.PointLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene11_PointLight extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 15), Vec.of(0, 0, -1),
                500, 500, toRadians(30)));

        addLight(new PointLight(Vec.point(0f, 2, 1f), Color.of(1f, 0f, 0f)));
        addLight(new PointLight(Vec.point(2f, 0f, 1f), Color.of(0f, 1f, 0f)));
        addLight(new PointLight(Vec.point(0f, 0f, 2f), Color.of(0f, 0f, 1f)));
        addLight(new PointLight(Vec.point(-.9f, -.9f, -.9f), Color.of(0f, 1f, 0f)));

        Color silver = Color.of(1, 1, 1);
        addObject(new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f)), Material.plastic(silver, 0f));

        Material checkerBoard = Material.checkerBoard(1);
        addObject(new HalfSpace(Vec.of(1f, 0f, 0f, 1.0001f)), checkerBoard);
        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 1.0001f)), checkerBoard);
        addObject(new HalfSpace(Vec.of(0f, 0f, 1f, 1.0001f)), checkerBoard);
    }
}
