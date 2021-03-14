package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Polygon;
import rausku.geometry.PolygonMesh;
import rausku.geometry.Vertex;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.FloatMath;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.List;

import static rausku.math.FloatMath.toRadians;

public class Scene16_Spiral extends DefaultSceneDefinition {
    {

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), Color.of(1f, 1f, 1f)));

        setCamera(Camera.createCamera(
                Vec.point(5, 5, 5),
                Vec.of(-1, -1, -1),
                500, 500,
                toRadians(45)));

        Material creamCeramic = Material.plastic(Color.of(1f, .9f, .8f), .1f);

        List<Polygon> polygons = createPolys();
        PolygonMesh teapot = new PolygonMesh(polygons);
        addObject(//Matrix.rotateY(toRadians(30)),
                teapot, creamCeramic);

//        addObject(HalfSpace.horizontalPlane(-1.00001f, new CheckerBoard(.5f)));
    }

    private List<Polygon> createPolys() {
        float R = 2, r = 1f;
        List<Polygon> polygons = new ArrayList<>();
        List<Vertex> previous = new ArrayList<>();

        int uResolution = 8;
        int vResolution = 4;

        for (int i = -uResolution; i <= uResolution; i++) {
            List<Vertex> current = new ArrayList<>();
            float u = i * 2 * FloatMath.PI / uResolution;
            float cosU = FloatMath.cos(u);
            float sinU = FloatMath.sin(u);

            float xCenter = R * cosU;
            float yCenter = u / 2;
            float zCenter = R * sinU;

            r = (float) i / uResolution;

            Vec center = Vec.point(xCenter, yCenter, zCenter);

            System.out.println(center);

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

                System.out.println(point);
                current.add(vertex);
            }
            previous = current;
        }
        return polygons;
    }
}
