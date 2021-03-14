package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene2_AmbientLightWithReflectingObjects extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(8, 8, 8),
                Vec.point(1, 0, 1),
                500, 500,
                toRadians(30)));

        addLight(new AmbientLight(Color.of(10f)));

        Material mirror = Material.mirror();
        Material matteRed = Material.matte(Color.of(.10f, .01f, .01f));
        Material matteWhite = Material.matte(Color.of(.10f));

        QuadraticForm sphere = QuadraticForm.createSphere(1f);
        addObject(sphere, matteRed);

        Matrix translate = Matrix.translate(0f, 0f, +3f);
        for (int i = 0; i < 360; i += 60) {
            Matrix rotateY = Matrix.rotateY(toRadians(i));
            addObject(Matrix.mul(translate, rotateY), sphere, mirror);
        }

        HalfSpace plane = HalfSpace.horizontalPlane(-1f);
        addObject(plane, matteWhite);
    }
}
