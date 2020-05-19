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

public class Scene14_Cave extends Scene {
    {

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f), Color.of(1f, 1f, 1f)));

        Material material = Material.plastic(Color.of(1f, .9f, .8f), .2f);

        addObject(new CSGSubtraction(material,
                new HalfSpace(Vec.of(0f, 1f, 0f, 0f), null),
                new QuadraticForm(Matrix.diag(1f, 1f, 1f, -4f), null)));
    }
}
