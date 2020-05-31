package rausku.scenes;

import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Vec;

public class Scene1_ThreeSpheres extends Scene {
    {
        Color red = Color.of(.9f, .2f, .2f);
        Color green = Color.of(.2f, .9f, .2f);
        Color blue = Color.of(.2f, .2f, .9f);
        QuadraticForm sphere = QuadraticForm.createSphere(Vec.of(-.5f, 0, 5), .5f);
        QuadraticForm sphere2 = QuadraticForm.createSphere(Vec.of(.0f, 0, 5.866f), .5f);
        QuadraticForm sphere3 = QuadraticForm.createSphere(Vec.of(.5f, 0, 5), .5f);
        QuadraticForm sphere4 = QuadraticForm.createSphere(Vec.of(.0f, .8165f, 5.422f), .5f);
        HalfSpace plane = HalfSpace.horizontalPlane(-.5f);

        addObject(sphere, Material.plastic(red, .5f));
        addObject(sphere2, Material.plastic(red, .5f));
        addObject(sphere3, Material.plastic(blue, .5f));
        addObject(sphere4, Material.plastic(green, .5f));

        addObject(plane, Material.plastic(Color.of(.24f, .5f, .2f), 0f));
    }
}
