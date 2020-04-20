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
        setCamera(new Camera(Matrix.mul(
                Matrix.rotateY(toRadians(-30)),
                Matrix.rotateX(toRadians(-30)),
                Matrix.translate(0, 0, 15)),
                500, 500,
                toRadians(45)));

        addDirectionalLight(new DirectionalLight(Vec.of(1, -2, -1), Color.of(.8f, .8f, .7f)));

        Material plastic = Material.plastic(Color.of(.8f, .8f, .8f), .5f);

        addObject(Matrix.translate(-1.25f, 0, 0),
                new CSGSubtraction(plastic,
                        new Cube(null),
                        new Sphere(Vec.origin(), 1.4f, null)));

        addObject(Matrix.translate(+1.25f, 0, 0),
                new CSGIntersection(plastic,
                        new Cube(null),
                        new Sphere(Vec.origin(), 1.4f, null)));

        addObject(new HorizontalPlane(-1.25f));
    }
}
