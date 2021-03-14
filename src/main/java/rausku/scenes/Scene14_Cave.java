package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.CSGSubtraction;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene14_Cave extends DefaultSceneDefinition {
    {

        setCamera(Camera.createCamera(
                Vec.point(0, 2, 10),
                Vec.of(0, -1, -5),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f), Color.of(1f, 1f, 1f)));

        Material material = Material.plastic(Color.of(1f, .9f, .8f), .2f);

        addObject(new CSGSubtraction(
                new HalfSpace(Vec.of(0f, 1f, 0f, 0f)),
                new QuadraticForm(Matrix.diag(1f, 1f, 1f, -4f))), material);
    }
}
