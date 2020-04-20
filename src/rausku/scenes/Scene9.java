package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.CheckerBoard;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene9 extends Scene {
    public Scene9() {

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-10)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f)));

        Material material = Material.plastic(Color.of(.8f, .8f, .8f), .7f);

        addObject(new QuadraticForm(Matrix.diag(1f, 1f, 1f, -2f), material));

        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 2.0001f), new CheckerBoard(null)));
    }
}
