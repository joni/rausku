package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Camera;
import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Cube;
import com.jsalonen.raytrace.geometry.HorizontalPlane;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.math.Matrix;
import com.jsalonen.raytrace.math.Vec;

import static com.jsalonen.raytrace.math.FloatMath.toRadians;

public class Scene3 extends Scene {
    public Scene3() {
        addObject(new Cube(Material.plastic(Color.of(1, 1, 1), .8f)));
        addObject(new Sphere(Vec.of(0, 0, 0f), 1.2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
        addObject(new HorizontalPlane(-1));

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 10)),
                500, 500,
                toRadians(45));
    }
}
