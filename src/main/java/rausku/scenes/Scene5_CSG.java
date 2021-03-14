package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.*;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene5_CSG extends DefaultSceneDefinition {
    public Scene5_CSG() {
        setCamera(Camera.createCamera(
                Vec.point(-6, 6, 12),
                Vec.of(1, -1, -2),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(1, -2, -1), Color.of(.8f, .8f, .7f)));

        Material plastic = Material.plastic(Color.of(1f, 1f, 1f), .3f);

        Cube cube = new Cube();
        QuadraticForm sphere = QuadraticForm.createSphere(Vec.origin(), 1.33f);

        addObject(Matrix.translate(-2.33f, 0, 0),
                new CSGSubtraction(cube, sphere), plastic);

        addObject(Matrix.translate(+0f, 0, 0),
                new CSGUnion(cube, sphere), plastic);

        addObject(Matrix.translate(+2.33f, 0, 0),
                new CSGIntersection(cube, sphere), plastic);

        addObject(HalfSpace.horizontalPlane(-1.001f), Material.plastic(Color.of(.24f, .5f, .2f), 0f));
    }
}
