package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.Polygon;
import rausku.geometry.PolygonMesh;
import rausku.io.ObjReader;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.FloatMath;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static rausku.math.FloatMath.toRadians;

public class Scene7_Teapot extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 200, 400),
                Vec.of(0, -2, -4),
                Vec.of(0, 1, 0),
                700, 700,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.of(+1f, -2f, +1f), FloatMath.PI / 8, Color.of(1f, 1f, 1f)));
        addLight(new DirectionalLight(Vec.of(-1f, -2f, -1f), FloatMath.PI / 8, Color.of(1f, 1f, 1f)));
        addLight(new AmbientLight(Color.of(.9f)));

        Material material = Material.lambertian(Color.of(1f, .9f, .8f));
//        Material material = Material.plastic(Color.of(1f, .9f, .8f), .1f);

        try (ObjReader reader = new ObjReader(new FileInputStream("data/teapot.obj"))) {
            List<Polygon> polygons = reader.read();
            PolygonMesh teapot = new PolygonMesh(polygons);
            addObject(Matrix.rotateY(toRadians(30)), teapot, material);
        } catch (IOException e) {
            e.printStackTrace();
        }

        addObject(HalfSpace.horizontalPlane(-39.9f), Material.gingham(10, Color.of(.75f, .5f, .5f)));

    }
}
