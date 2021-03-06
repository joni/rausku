package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene9_Antialias extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 2, 10),
                Vec.of(0, -1, -5),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f)));

        Material material = Material.plastic(Color.of(.8f, .8f, .8f), .7f);

        addObject(new QuadraticForm(Matrix.diag(1f, 1f, 1f, -2f)), material);

        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 2.0001f)), Material.checkerBoard(1));
    }
}
