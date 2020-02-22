package rausku.scenes;

import rausku.Camera;
import rausku.Color;
import rausku.Material;
import rausku.Scene;
import rausku.geometry.Cube;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.Intersection;
import rausku.geometry.Sphere;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene4 extends Scene {
    public Scene4() {

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 20)),
                500, 500,
                toRadians(45));

        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        addObject(new Intersection(plastic,
                new Cube(plastic),
                new Sphere(Vec.of(0, 0, 0f), 1.2f, plastic)));
        addObject(new HorizontalPlane(-1));
    }
}
