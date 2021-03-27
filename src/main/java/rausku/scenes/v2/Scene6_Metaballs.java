package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.Geometry;
import rausku.geometry.HalfSpace;
import rausku.geometry.Metaballs;
import rausku.geometry.StandardObjects;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import java.util.List;

import static rausku.math.FloatMath.PI;
import static rausku.math.FloatMath.toRadians;

public class Scene6_Metaballs extends DefaultSceneDefinition {
    public Scene6_Metaballs() {
        setCamera(Camera.lookAt(
                Vec.point(0, 5, 10),
                Vec.origin(),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.unit(+1, -1, -1), Color.of(5f)));
        addLight(new AmbientLight(Color.of(5f)));

        Material red = Material.lambertian(Color.of(1 / 8f, .01f, .01f));
        Material white = Material.lambertian(Color.of(1 / 8f));
        Material mirror = Material.mirror();

        Geometry sphere = StandardObjects.sphere();
        Geometry cube = StandardObjects.cube();
        Metaballs metaballs = new Metaballs(List.of(
                Vec.point(0, 0, 1),
                Vec.point(0, 0, -1),
                Vec.point(0, 1, 0),
                Vec.point(0, -1, 0),
                Vec.point(1, 0, 0),
                Vec.point(-1, 0, 0),
                Vec.point(0, 0, 0)
        ));

        addObject(Matrix.translate(-2.25f, 0, 0), sphere, red);
        addObject(Matrix.rotate(Vec.unit(1, 0, 1), 2 * PI / 6), metaballs, mirror);
        addObject(Matrix.mul(Matrix.translate(+2.25f, 0, 0)), cube, red);
        addObject(HalfSpace.horizontalPlane(-1), white);
    }
}
