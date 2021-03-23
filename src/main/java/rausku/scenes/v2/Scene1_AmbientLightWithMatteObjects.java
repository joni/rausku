package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.Cube;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene1_AmbientLightWithMatteObjects extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(2, 4, 8),
                Vec.of(-2, -4, -8),
                500, 500,
                toRadians(30)));

        addLight(new AmbientLight(Color.of(15f)));

        Material matteRed = Material.lambertian(Color.of(.10f, .01f, .01f));
        Material matteWhite = Material.lambertian(Color.of(.10f));
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
