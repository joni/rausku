package rausku.scenes;

import rausku.Camera;
import rausku.Color;
import rausku.Material;
import rausku.Scene;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.QuadraticForm;
import rausku.math.Matrix;

import static rausku.math.FloatMath.toRadians;

public class Scene8 extends Scene {
    public Scene8() {

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-15)), Matrix.translate(0, 0, 20)),
                500, 500,
                toRadians(45));

        Material material = Material.plastic(Color.of(.8f, .8f, .8f), .0f);

        addObject(new QuadraticForm(Matrix.diag(1f, -.25f, 1f, 1f), material));
        addObject(new QuadraticForm(Matrix.diag(.5f, 2f, .5f, 1f), material));

        addObject(new HorizontalPlane(-2));
    }
}
