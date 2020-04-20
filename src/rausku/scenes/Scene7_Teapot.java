package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.Obj;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.CheckerBoard;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.io.IOException;

import static rausku.math.FloatMath.toRadians;

public class Scene7_Teapot extends Scene {
    {

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), Color.of(1f, 1f, 1f)));

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 500)),
                500, 500,
                toRadians(45)));

        Material creamCeramic = Material.plastic(Color.of(1f, .9f, .8f), .1f);

        try {
            Obj teapot = new Obj("data/teapot.obj", creamCeramic);
            addObject(Matrix.rotateY(toRadians(30)), teapot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addObject(new HorizontalPlane(-49.0001f, new CheckerBoard(50)));

    }
}
