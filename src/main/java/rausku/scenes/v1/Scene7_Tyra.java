package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.Polygon;
import rausku.geometry.PolygonMesh;
import rausku.io.ObjReader;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static rausku.math.FloatMath.toRadians;

public class Scene7_Tyra extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 4, 8),
                Vec.of(0, -1, -2),
                Vec.of(0, 1, 0),
                700, 700,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), Color.of(1f, 1f, 1f)));

        Material material = Material.plastic(Color.of(1f, .9f, .8f), .1f);

        try (ObjReader reader = new ObjReader(new FileInputStream("data/tyra.obj"))) {
            List<Polygon> polygons = reader.read();
            PolygonMesh teapot = new PolygonMesh(polygons);
            addObject(Matrix.rotateY(toRadians(-90)), teapot, material);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addObject(HalfSpace.horizontalPlane(-1.34f), Material.gingham(1, Color.of(.75f, .5f, .5f)));

    }
}
