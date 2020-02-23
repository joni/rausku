package rausku.scenes;

import rausku.Camera;
import rausku.Color;
import rausku.Scene;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.Obj;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.io.IOException;

import static rausku.math.FloatMath.toRadians;

public class Scene7 extends Scene {
    public Scene7() {

        directionalLight = new DirectionalLight(Vec.of(.5f, -.75f, +.5f).normalize(), Color.of(.8f, .8f, .8f));

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 500)),
                500, 500,
                toRadians(45));

        try {
            Obj teapot = new Obj("data/teapot.obj");
            addObject(Matrix.rotateY(toRadians(30)), teapot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addObject(new HorizontalPlane(-50f));

    }
}
