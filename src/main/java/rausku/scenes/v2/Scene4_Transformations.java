package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.PI;
import static rausku.math.FloatMath.toRadians;

public class Scene4_Transformations extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(0, 7, 12),
                Vec.point(0, 1, 0),
                500, 500,
                toRadians(45)));

//        addLight(new DirectionalLight(Vec.unit(1, -1, -1), PI/32, Color.of(10f)));
        addLight(new AmbientLight(Color.of(10f)));

        Material white = Material.lambertian(Color.of(.125f));
        Material mirror = Material.mirror();
        Material red = Material.lambertian(Color.of(1 / 8f, 1 / 128f, 1 / 128f));
        Material objectMat = Material.mirror();
        Material glossy = Material.glossy(Color.of(.8f), PI / 8);
        SceneObject object = QuadraticForm.createSphere(1f);

        addObject(Matrix.mul(Matrix.translate(0, 0, -3), Matrix.diag(5f, 5f, 1, 1)), object, objectMat);
        addObject(Matrix.mul(Matrix.translate(-3, 0, 0), Matrix.diag(1, 5f, 1, 1)), object, objectMat);
        addObject(Matrix.mul(Matrix.rotateY(toRadians(135))), object, objectMat);
        addObject(Matrix.mul(Matrix.translate(3, 0, 1.5f), Matrix.diag(1, 1f, 2.5f, 1)), object, objectMat);
        addObject(Matrix.mul(Matrix.translate(-1.5f, 0, 3), Matrix.rotateZ(toRadians(90)), Matrix.diag(1, 2.5f, 1, 1)), object, objectMat);


        addObject(Matrix.mul(Matrix.translate(+6, 0, 0), Matrix.diag(1f, 10f, 10f, 1)), object, red);

        addObject(HalfSpace.horizontalPlane(-1f), white);
    }
}
