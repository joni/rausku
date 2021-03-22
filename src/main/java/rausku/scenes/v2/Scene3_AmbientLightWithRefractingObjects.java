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

public class Scene3_AmbientLightWithRefractingObjects extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(0, 0, 15),
                Vec.point(0, 0, 0),
                500, 500,
                toRadians(30)));

        addLight(new AmbientLight(Color.of(10f)));

        Material glass = Material.glass();
        Material matteWhite = Material.matte(Color.of(.10f));
        Material matteRed = Material.matte(Color.of(.10f, .01f, .01f));

        QuadraticForm sphere = QuadraticForm.createSphere(1f);
        addObject(Matrix.translate(0f, 0f, +10f), sphere, glass);

        Cube cube = new Cube();
        for (int x = -3; x <= 3; x += 3) {
            for (int y = -3; y <= 3; y += 3) {
                addObject(Matrix.translate(x, y, 0), sphere, matteRed);
            }
        }

        HalfSpace plane = new HalfSpace(Vec.of(0, 0, 1, 1));
        addObject(plane, matteWhite);
    }
}
