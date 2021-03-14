package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene4_Transforms extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 7, 12),
                Vec.of(0, -1, -2),
                500, 500,
                toRadians(45)));

        Material material = Material.plastic(Color.of(1, 1, 1), .3f);
        SceneObject object1 = QuadraticForm.createSphere(Vec.origin(), 1f);

        addObject(Matrix.mul(Matrix.translate(0, 0, -3), Matrix.diag(5f, 5f, 1, 1)), object1, material);
        addObject(Matrix.mul(Matrix.translate(-3, 0, 0), Matrix.diag(1, 4, 1, 1)), object1, material);
        addObject(Matrix.mul(Matrix.rotateY(toRadians(135))), object1, material);
        addObject(Matrix.mul(Matrix.translate(3, 0, 1.5f), Matrix.rotateX(toRadians(90)), Matrix.diag(1, 2.5f, 1, 1)), object1, material);
        addObject(Matrix.mul(Matrix.translate(-1.5f, 0, 3), Matrix.rotateZ(toRadians(90)), Matrix.diag(1, 2.5f, 1, 1)), object1, material);

        addObject(HalfSpace.horizontalPlane((float) -1), Material.plastic(Color.of(.24f, .5f, .2f), 0f));
    }
}
