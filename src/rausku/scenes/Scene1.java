package rausku.scenes;

import rausku.geometry.HorizontalPlane;
import rausku.geometry.Sphere;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Vec;

public class Scene1 extends Scene {

    public Scene1() {
        Color red = Color.of(.9f, .2f, .2f);
        Color green = Color.of(.2f, .9f, .2f);
        Color blue = Color.of(.2f, .2f, .9f);
        Sphere sphere = new Sphere(Vec.of(-.5f, 0, 5), .5f, Material.plastic(red, .5f));
        Sphere sphere2 = new Sphere(Vec.of(.0f, 0, 5.866f), .5f, Material.plastic(red, .5f));
        Sphere sphere3 = new Sphere(Vec.of(.5f, 0, 5), .5f, Material.plastic(blue, .5f));
        Sphere sphere4 = new Sphere(Vec.of(.0f, .8165f, 5.422f), .5f, Material.plastic(green, .5f));
        HorizontalPlane plane = new HorizontalPlane(-.5f);

        addObject(sphere);
        addObject(sphere2);
        addObject(sphere3);
        addObject(sphere4);

        addObject(plane);
    }
}
