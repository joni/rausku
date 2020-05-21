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

public class Scene8_QuadraticForms extends Scene {
    {

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f)));

        Material material = Material.plastic(Color.of(.8f, .8f, .8f), .7f);

        addObject(new QuadraticForm(Matrix.diag(1f, -.2f, 1f, -.1f)), material);
        addObject(new QuadraticForm(Matrix.diag(.5f, 2f, .5f, -1f)), material);

        addObject(HalfSpace.horizontalPlane(-.2f), new CheckerBoard(1));
    }
}
