package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene0_Sphere extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 2, 8),
                Vec.of(0, -2, -8),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(+1, -1, 0), 0, Color.of(50f)));

        QuadraticForm sphere = QuadraticForm.createSphere(1f);
        addObject(sphere, Material.solidColorMatte(Color.of(.20f, .01f, .01f)));

        HalfSpace plane = HalfSpace.horizontalPlane(-1f);
        addObject(plane, Material.solidColorMatte(Color.of(.02f)));
    }
}
