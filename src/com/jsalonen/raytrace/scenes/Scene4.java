package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Cube;
import com.jsalonen.raytrace.geometry.HorizontalPlane;
import com.jsalonen.raytrace.geometry.Intersection;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.math.Vec;

class Scene4 extends Scene {
    public Scene4() {
        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        objects.add(new Intersection(plastic,
                new Cube(plastic),
                new Sphere(Vec.of(0, 0, 0f), 1.2f, plastic)));
        objects.add(new HorizontalPlane(-1));
    }
}
