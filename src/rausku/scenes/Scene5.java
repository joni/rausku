package rausku.scenes;

import rausku.Color;
import rausku.Material;
import rausku.Scene;
import rausku.geometry.*;
import rausku.math.Vec;

public class Scene5 extends Scene {
    public Scene5() {
        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        addObject(new Subtraction(plastic,
                new Cube(Vec.of(.75f, 0, 0f), plastic),
                new Sphere(Vec.of(.75f, 0, 0f), 1.4f, plastic)));

        addObject(new Intersection(plastic,
                new Cube(Vec.of(-.75f, 0, 0f), plastic),
                new Sphere(Vec.of(-.75f, 0, 0f), 1.2f, plastic)));

        addObject(new HorizontalPlane(-1));
    }
}
