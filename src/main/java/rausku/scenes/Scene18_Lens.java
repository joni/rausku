package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene18_Lens extends DefaultSceneDefinition {
    {

        setCamera(Camera.createCamera(
                Vec.point(5, 5f, 10f),
                Vec.of(-1, -1, -2),
                Vec.of(0, 1, 0),
                700, 700,
                toRadians(30)));

        SceneObject sphere = QuadraticForm.createSphere(Vec.origin(), 3);

        Material mirror = Material.metallic(Color.of(1, 1, 1), 1);
        addObject(Matrix.diag(1f, 1f, 1 / 8f, 1), sphere, Material.glass());

        SceneObject sphere2 = QuadraticForm.createSphere(Vec.origin(), .5f);
        for (int i = 0; i < 360; i += 30) {
            Matrix rotateY = Matrix.rotateY(toRadians(i));
            Material material = Material.plastic(Color.ofHsl(i / 360f, 1f, 5f), .3f);
            addObject(Matrix.mul(rotateY, Matrix.translate(0f, +.5f, -3f)), sphere2, material);
        }

        addObject(HalfSpace.horizontalPlane(-0.0001f), Material.checkerBoard(1));
    }
}
