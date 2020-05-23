package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene0_Sphere extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 4),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        Color red = Color.of(.9f, .2f, .2f);
        QuadraticForm sphere = QuadraticForm.createSphere(Vec.origin(), .5f);
        addObject(sphere, Material.plastic(red, 0f));
    }
}
