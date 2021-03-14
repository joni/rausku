package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.Polygon;
import rausku.geometry.PolygonMesh;
import rausku.io.PLYReader;
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

public class Scene17_Dragon extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, .7f, .6f),
                Vec.of(0, -1, -1),
                Vec.of(0, 1, 0),
                700, 700,
                toRadians(20)));

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), Color.of(1f, 1f, 1f)));

        Material material = Material.plastic(Color.of(1f, .1f, .1f), .1f);

        try (PLYReader reader = new PLYReader(new FileInputStream("data/dragon_vrip.ply"))) {
            System.out.println("Reading file");
            List<Polygon> polygons = reader.read();
            System.out.println("Constructing mesh");
            PolygonMesh object = new PolygonMesh(polygons);
            System.out.println("Done");
            addObject(Matrix.rotateY(toRadians(30)), object, material);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addObject(HalfSpace.horizontalPlane(+0.052f), Material.checkerBoard(.05f));

    }
}
