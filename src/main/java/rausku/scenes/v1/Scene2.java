package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene2 extends DefaultSceneDefinition {

    public Scene2() {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 10),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                addObject(QuadraticForm.createSphere(Vec.point(i, j, -10), .5f), Material.plastic(Color.of((i + 5) / 10f, (j + 5) / 10f, (10 - i - j) / 20f), .5f));
            }
        }

        addObject(new QuadraticForm(Matrix.diag(1f, 1f, 1f, -3f)), Material.glass());
    }
}
