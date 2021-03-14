package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.PointLight;
import rausku.material.Material;
import rausku.math.FloatMath;
import rausku.math.Matrix;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene0_ReflectingSphere extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 2, 8),
                Vec.of(0, -2, -8),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(+1, -1, 0), FloatMath.PI / 8, Color.of(30f)));
        addLight(new PointLight(Vec.of(0, 4, 0), Color.of(2f)));

        Color red = Color.of(.10f, .01f, .01f);
        Color white = Color.of(.1f);
        QuadraticForm sphere = QuadraticForm.createSphere(1f);
        addObject(Matrix.translate(-1, 0, 0), sphere, Material.solidColor(red, white, .8f));
        addObject(Matrix.translate(+1, 0, 0), sphere, Material.solidColor(white, white, .8f));

        HalfSpace plane = HalfSpace.horizontalPlane(-1f);
        addObject(plane, Material.matte(Color.of(.02f)));
    }
}
