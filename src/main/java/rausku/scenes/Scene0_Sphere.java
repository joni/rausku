package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Vec;

import static rausku.math.FloatMath.toRadians;

public class Scene0_Sphere extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 4),
                Vec.of(0, -0, -4),
                500, 500,
                toRadians(30)));

        Color red = Color.of(.9f, .2f, .2f);
        QuadraticForm sphere = QuadraticForm.createSphere(Vec.origin(), .5f);
        addObject(sphere, Material.plastic(red, 0f));

        HalfSpace plane = HalfSpace.horizontalPlane(-.5f);
        addObject(plane, Material.plastic(Color.of(.9f), 0f));
    }

    @Override
    public AmbientLight getAmbientLight() {
        return new AmbientLight(Color.of(2f));
    }
}
