package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.QuadraticForm;
import rausku.material.MaterialX;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene6 extends Scene {

    public Scene6() {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 15),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        addObject(QuadraticForm.createSphere(Vec.point(2, 0, -5f), 2f), new MaterialX());
//        addObject(new BumpySphere(Vec.point(-2, 0, -5f), 2f), Material.plastic(Color.of(.8f, .2f, .2f), .9f));
    }
}
