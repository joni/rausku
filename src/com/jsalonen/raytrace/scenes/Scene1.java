package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.HorizontalPlane;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.geometry.Vec;

class Scene1 extends Scene {

    public Scene1() {
        Color red = Color.of(.9f, .2f, .2f);
        Color green = Color.of(.2f, .9f, .2f);
        Color blue = Color.of(.2f, .2f, .9f);
        Sphere sphere = new Sphere(Vec.of(-.5f, 0, 5), .5f, Material.plastic(red, .5f));
        Sphere sphere2 = new Sphere(Vec.of(.0f, 0, 5.866f), .5f, Material.plastic(red, .5f));
        Sphere sphere3 = new Sphere(Vec.of(.5f, 0, 5), .5f, Material.plastic(blue, .5f));
        Sphere sphere4 = new Sphere(Vec.of(.0f, .8165f, 5.422f), .5f, Material.plastic(green, .5f));
        HorizontalPlane plane = new HorizontalPlane(-.5f);

        objects.add(sphere);
        objects.add(sphere2);
        objects.add(sphere3);
        objects.add(sphere4);

        objects.add(plane);
    }
}
