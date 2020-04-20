package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.CheckerBoard;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene8 extends Scene {
    public Scene8() {

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45)));

        addDirectionalLight(new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f)));

        Material material = Material.plastic(Color.of(.8f, .8f, .8f), .7f);

        addObject(new QuadraticForm(Matrix.diag(1f, -.2f, 1f, -.1f), material));
        addObject(new QuadraticForm(Matrix.diag(.5f, 2f, .5f, -1f), material));

        addObject(new HorizontalPlane(-.2f, new CheckerBoard(null)));
    }
}
