package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.Polygon;
import rausku.geometry.Torus;
import rausku.geometry.Vertex;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.FloatMath;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import java.util.ArrayList;
import java.util.List;

import static rausku.math.FloatMath.toRadians;

public class Scene15_Torus extends DefaultSceneDefinition {
    {

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), Color.of(1f, 1f, 1f)));

        setCamera(Camera.createCamera(
                Vec.point(0, 10, 10),
                Vec.of(0, -1, -1),
                500, 500,
                toRadians(45)));

        Material material1 = Material.plastic(Color.of(.8f, 0f, .6f), .1f);
        Material material2 = Material.plastic(Color.of(.6f, 0f, .8f), .1f);

        Torus torus = new Torus(2, 1f);

        addObject(Matrix.translate(-1, 0, 0), torus, material1);
        addObject(Matrix.mul(Matrix.rotateX(toRadians(90)), Matrix.translate(+1, 0, 0)), torus, material2);

        addObject(HalfSpace.horizontalPlane(-3.00001f), Material.checkerBoard(.5f));
    }

    private List<Polygon> createPolys() {
        float R = 1, r = .5f;
        List<Polygon> polygons = new ArrayList<>();
        List<Vertex> previous = new ArrayList<>();

        int uResolution = 32;
        int vResolution = 16;

        for (int i = 0; i <= uResolution; i++) {
            List<Vertex> current = new ArrayList<>();
            float u = i * 2 * FloatMath.PI / uResolution;
            float cosU = FloatMath.cos(u);
            float sinU = FloatMath.sin(u);

            float xCenter = R * cosU;
            float zCenter = R * sinU;

            Vec center = Vec.point(xCenter, 0, zCenter);

            for (int j = 0; j <= vResolution; j++) {

                float v = j * 2 * FloatMath.PI / vResolution;
                float cosV = FloatMath.cos(v);
                float sinV = FloatMath.sin(v);

                float x = sinV * cosU;
                float y = cosV;
                float z = sinV * sinU;

                Vec normal = Vec.of(x, y, z);
                Vec point = Vec.mulAdd(r, normal, center);

                Vertex vertex = Vertex.of(point, normal);

                if (i > 0 && j > 0) {
                    Vertex vec0 = previous.get(j - 1);
                    Vertex vec1 = previous.get(j);
                    Vertex vec2 = current.get(j - 1);

                    polygons.add(new Polygon(vertex, vec0, vec1));
                    polygons.add(new Polygon(vertex, vec2, vec0));
                }

                current.add(vertex);
            }
            previous = current;
        }
        return polygons;
    }
}
