package rausku.scenes;

import rausku.Camera;
import rausku.Color;
import rausku.Scene;
import rausku.geometry.HorizontalPlane;
import rausku.geometry.Obj;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.io.IOException;

import static rausku.math.FloatMath.toRadians;

public class Scene7 extends Scene {
    public Scene7() {

        directionalLight = new DirectionalLight(Vec.of(.5f, -.75f, +.5f).normalize(), Color.of(.8, .8, .8));

        camera = new Camera(
                Matrix.mul(Matrix.rotateX(toRadians(-30)), Matrix.translate(0, 0, 500)),
                500, 500,
                toRadians(45));

        try {
            Obj teapot = new Obj("data/teapot.obj");
            addObject(Matrix.rotateY(toRadians(30)), teapot);
//            addObject(Matrix.translate(100, 0, 0), teapot);
//            addObject(Matrix.translate(-100, 0, 0), teapot);
//            addObject(Matrix.translate(0, 0, -100), teapot);
//            addObject(Matrix.translate(100, 0, -100), teapot);
//            addObject(Matrix.translate(-100, 0, -100), teapot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        addObject(new HorizontalPlane(-50f));

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
