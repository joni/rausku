package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Camera;
import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Cube;
import com.jsalonen.raytrace.geometry.HorizontalPlane;
import com.jsalonen.raytrace.geometry.Intersection;
import com.jsalonen.raytrace.geometry.Sphere;
import com.jsalonen.raytrace.math.Matrix;
import com.jsalonen.raytrace.math.Vec;

import static com.jsalonen.raytrace.math.FloatMath.toRadians;

public class Scene4 extends Scene {
    public Scene4() {

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 20)),
                500, 500,
                toRadians(45));

        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        addObject(new Intersection(plastic,
                new Cube(plastic),
                new Sphere(Vec.of(0, 0, 0f), 1.2f, plastic)));
        addObject(new HorizontalPlane(-1));
    }
}
