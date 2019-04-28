package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.geometry.Vec;

class Scene2 extends Scene {

    public Scene2() {
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                objects.add(new Sphere(Vec.of(i, j, 10), .5f, Material.plastic(Color.of((i + 5) / 10f, (j + 5) / 10f, (10 - i - j) / 20f), .5f)));
            }
        }

        objects.add(new Sphere(Vec.of(0, 0, 5f), 1f, Material.glass()));
    }
}
