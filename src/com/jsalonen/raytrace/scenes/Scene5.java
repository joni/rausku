package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.*;

class Scene5 extends Scene {
    public Scene5() {
        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        objects.add(new Subtraction(plastic,
                new Cube(Vec.of(.75f, 0, 0f), plastic),
                new Sphere(Vec.of(.75f, 0, 0f), 1.4f, plastic)));

        objects.add(new Intersection(plastic,
                new Cube(Vec.of(-.75f, 0, 0f), plastic),
                new Sphere(Vec.of(-.75f, 0, 0f), 1.2f, plastic)));

        objects.add(new HorizontalPlane(-1));
    }
}
