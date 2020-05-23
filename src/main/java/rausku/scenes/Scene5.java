package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.*;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene5 extends Scene {
    public Scene5() {
        setCamera(Camera.createCamera(
                Vec.point(-6, 6, 12),
                Vec.of(1, -1, -2),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(1, -2, -1), Color.of(.8f, .8f, .7f)));

        Material plastic = Material.plastic(Color.of(1f, 1f, 1f), .3f);

        addObject(Matrix.translate(-1.25f, 0, 0),
                new CSGSubtraction(
                        new Cube(),
                        QuadraticForm.createSphere(Vec.origin(), 1.33f)), plastic);

        addObject(Matrix.translate(+1.25f, 0, 0),
                new CSGIntersection(
                        new Cube(),
                        QuadraticForm.createSphere(Vec.origin(), 1.33f)), plastic);

        addObject(HalfSpace.createHorizontalPlane(-1.001f), Material.plastic(Color.of(.24f, .5f, .2f), 0f));
    }
}
