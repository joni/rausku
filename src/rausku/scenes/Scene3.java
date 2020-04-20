package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Cube;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.Sphere;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene3 extends Scene {
    public Scene3() {
        addObject(new Cube(Material.plastic(Color.of(1, 1, 1), .8f)));
        addObject(new Sphere(Vec.of(0, 0, 0f), 1.2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
        addObject(new HorizontalPlane(-1));

        setCamera(new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45)));
    }
}
