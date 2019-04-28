package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.MaterialX;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.BumpySphere;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.geometry.Vec;

public class Scene6 extends Scene {

    public Scene6() {
        objects.add(new Sphere(Vec.of(2, 0, 5f), 2f, new MaterialX(null)));
        objects.add(new BumpySphere(Vec.of(-2, 0, 5f), 2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
    }
}
