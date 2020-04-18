package rausku.scenes;

import rausku.*;
import rausku.geometry.BumpySphere;
import rausku.geometry.Sphere;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene6 extends Scene {

    public Scene6() {
        camera = new Camera(
                Matrix.translate(0, 0, 15),
                500, 500,
                toRadians(45));

        addObject(new Sphere(Vec.point(2, 0, -5f), 2f, new MaterialX()));
        addObject(new BumpySphere(Vec.point(-2, 0, -5f), 2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
    }
}
