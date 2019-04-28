package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Cube;
import com.jsalonen.raytrace.geometry.HorizontalPlane;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.geometry.Vec;

class Scene3 extends Scene {
    public Scene3() {
        objects.add(new Cube(Material.plastic(Color.of(1, 1, 1), .8f)));
        objects.add(new Sphere(Vec.of(0, 0, 0f), 1.2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
        objects.add(new HorizontalPlane(-1));
    }
}
