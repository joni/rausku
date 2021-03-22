package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.CSGIntersection;
import rausku.geometry.Cube;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene5_CSG extends DefaultSceneDefinition {
    public Scene5_CSG() {
        setCamera(Camera.createCamera(
                Vec.point(-6, 6, 12),
                Vec.of(1, -1, -2),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(1, -2, -1), Color.of(.8f, .8f, .7f)));

        Material red = Material.matte(Color.of(1 / 8f, .01f, .01f));
        Material white = Material.matte(Color.of(1 / 8f));

        Cube cube = new Cube();
        QuadraticForm sphere = QuadraticForm.createSphere(1.33f);

//        addObject(Matrix.translate(-2.33f, 0, 0),
//                new CSGSubtraction(cube, sphere), plastic);
//
//        addObject(Matrix.translate(+0f, 0, 0),
//                new CSGUnion(cube, sphere), plastic);

        addObject(//Matrix.translate(+2.33f, 0, 0),
                new CSGIntersection(cube, sphere), red);

        addObject(HalfSpace.horizontalPlane(-1.001f), white);
    }
}
