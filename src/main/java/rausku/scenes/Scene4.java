package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.CSGIntersection;
import rausku.geometry.Cube;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene4 extends Scene {
    public Scene4() {

        setCamera(Camera.createCamera(
                Vec.point(0, 5, 10),
                Vec.of(0, -1, -2),
                500, 500,
                toRadians(45)));

        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        addObject(new CSGIntersection(
                new Cube(),
                QuadraticForm.createSphere(Vec.of(0, 0, 0f), 1.2f)), plastic);

        addObject(HalfSpace.createHorizontalPlane(-1), Material.plastic(Color.of(.24f, .5f, .2f), 0f));
    }
}
