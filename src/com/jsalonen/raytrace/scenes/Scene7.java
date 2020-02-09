package com.jsalonen.raytrace.scenes;

import com.jsalonen.raytrace.Camera;
import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Scene;
import com.jsalonen.raytrace.geometry.Obj;
import com.jsalonen.raytrace.lighting.DirectionalLight;
import com.jsalonen.raytrace.math.Matrix;
import com.jsalonen.raytrace.math.Vec;

import java.io.IOException;

import static com.jsalonen.raytrace.math.FloatMath.toRadians;

public class Scene7 extends Scene {
    public Scene7() {

        directionalLight = new DirectionalLight(Vec.of(1, -2, -1).normalize(), Color.of(1, 1, 1));

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-15)), Matrix.translate(0, 10, 500)),
                500, 500,
                toRadians(45));

        try {
            objects.add(new Obj("data/teapot.obj"));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Vec o = Vec.of(10, 10, -50);
//        Vertex ov = Vertex.of(o, Vec.of(0, 0, -1));
        Vec up = Vec.of(0, 1, 0);
        Vec down = Vec.of(0, -1, 0);
        Vec left = Vec.of(-1, 0, 0);
        Vec right = Vec.of(1, 0, 0);
//        objects.add(new Obj(List.of(
//                new Polygon(ov, Vertex.of(Vec.of(0, 100, 0), down), Vertex.of(Vec.of(-100, 0, 0), right)),
//                new Polygon(ov, Vertex.of(Vec.of(-100, 0, 0), right), Vertex.of(Vec.of(0, -100, 0), up)),
//                new Polygon(ov, Vertex.of(Vec.of(100, 0, 0), left), Vertex.of(Vec.of(0, 100, 0), down)),
//                new Polygon(ov, Vertex.of(Vec.of(0, -100, 0), up), Vertex.of(Vec.of(100, 0, 0), left))
//        )));

//        Vec o = Vec.of(0, -100, 100);
//        Vertex ov = Vertex.of(o, up);
//
//        objects.add(new Obj(List.of(
////                new Polygon(ov, Vertex.of(Vec.of(0, -100, 50), up), Vertex.of(Vec.of(100, -100, 75), up)),
//                new Polygon(
//                        Vertex.of(Vec.of(-100, -100, 10), Vec.of(-.7, .7, 0)),
//                        Vertex.of(Vec.of(100, -100, 10), Vec.of(+.7, .7, 0)),
//                        Vertex.of(Vec.of(0, -100, 100), Vec.of(0, .7, -.7)))
//        )));

//        HorizontalPlane plane = new HorizontalPlane(-100);
//        objects.add(plane);
    }
}
