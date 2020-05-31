package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene10_Materials extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 10),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f)));

        Color silver = Color.of(.8f, .8f, .8f);
        QuadraticForm object = new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f));

        addObject(Matrix.translate(-2, 2, 0), object, Material.matte(silver));
        addObject(Matrix.translate(0, 2, 0), object, Material.metallic(silver, .99f));
        addObject(Matrix.translate(+2, 2, 0), object, Material.glass());

        addObject(Matrix.translate(-2, 0, 0), object, Material.gingham(1 / 8f, Color.of(1, 0, 1), Color.of(0, 1, 1)));

        addObject(new HalfSpace(Vec.of(0f, 0f, 1f, 1.0001f)), Material.checkerBoard(1));
    }
}
