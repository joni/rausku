package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Ginham;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene13_Hyperboloid extends Scene {
    {

        setCamera(Camera.createCamera(
                Vec.point(0, 2, 10),
                Vec.of(0, -1, -5),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -1f, -1f), Color.of(1f, 1f, 1f)));

        Material material = Material.plastic(Color.of(1f, .9f, .8f), .2f);

        Material checkerBoard = new Ginham(.5f, Color.of(.5f, .5f, .75f), Color.of(.75f, .5f, .5f));

        addObject(new QuadraticForm(false, Matrix.diag(1f, 1f, -4f, -1f)), material);
        addObject(new QuadraticForm(Matrix.diag(1f, 1f, 1f, -.25f)), material);
        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 1.25f)), checkerBoard);
    }
}
