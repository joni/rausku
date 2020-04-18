package rausku.scenes;

import rausku.*;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene10 extends Scene {
    public Scene10() {

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-10)), Matrix.translate(0, 0, 15)),
                500, 500,
                toRadians(45));

        directionalLight = new DirectionalLight(Vec.of(1f, -1f, -1f).normalize(), Color.of(.8f, .8f, .7f));

        Color silver = Color.of(.8f, .8f, .8f);
        addObject(Matrix.translate(-2.0001f, 0, 0),
                new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f), Material.plastic(silver, .99f)));

        addObject(Matrix.translate(0, 0, 0),
                new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f), Material.metallic(silver, .99f)));

        addObject(Matrix.translate(+2.0001f, 0, 0),
                new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f), Material.glass()));

        addObject(new HalfSpace(Vec.of(0f, 1f, 0f, 1.0001f), new CheckerBoard(null)));
    }
}
