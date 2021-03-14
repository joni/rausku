package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Cube;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene0_SphereAndCubes extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(2, 4, 8),
                Vec.of(-2, -4, -8),
                500, 500,
                toRadians(30)));

//        addLight(new DirectionalLight(Vec.of(+1, -1, 0), FloatMath.PI / 8, Color.of(30f)));
        addLight(new AmbientLight(Color.of(15f)));

        Material matteRed = Material.matte(Color.of(.10f, .01f, .01f));
        Material matteWhite = Material.matte(Color.of(.10f));
        QuadraticForm sphere = QuadraticForm.createSphere(1f);
        Cube cube = new Cube();
        addObject(sphere, matteRed);
        addObject(Matrix.translate(-2, 0, -2), cube, matteWhite);
        addObject(Matrix.translate(+2, 0, -2), cube, matteWhite);
        addObject(Matrix.translate(-2, 0, +2), cube, matteWhite);
        addObject(Matrix.translate(+2, 0, +2), cube, matteWhite);

        HalfSpace plane = HalfSpace.horizontalPlane(-1f);
        addObject(plane, matteWhite);
    }
}
