package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.*;
import com.jsalonen.raytrace.geometry.BumpySphere;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.math.Matrix;
import com.jsalonen.raytrace.math.Vec;

import static com.jsalonen.raytrace.math.FloatMath.toRadians;

public class Scene6 extends Scene {

    public Scene6() {
        camera = new Camera(
                Matrix.translate(0, 0, 15),
                500, 500,
                toRadians(45));

        addObject(new Sphere(Vec.of(2, 0, -5f), 2f, new MaterialX(null)));
        addObject(new BumpySphere(Vec.of(-2, 0, -5f), 2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
    }
}
