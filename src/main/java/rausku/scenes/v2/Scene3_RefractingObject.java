package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.Geometry;
import rausku.geometry.StandardObjects;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.PI;
import static rausku.math.FloatMath.toRadians;

public class Scene3_RefractingObject extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(10, 10, 10),
                Vec.point(0, 0, 0),
                500, 500,
                toRadians(30)));

        addLight(new DirectionalLight(Vec.unit(+1, -1, -1), PI / 16, Color.of(10f)));

        Material glass = Material.glass();
        Material red = Material.lambertian(Color.of(.8f, .2f, .2f));

        Geometry sphere = StandardObjects.sphere();
        addObject(Matrix.translate(7f, 7f, 7f), sphere, glass);

        for (int x = -2; x <= 1; x++) {
            for (int y = -2; y <= 1; y++) {
                addObject(Matrix.translate(3 * x, 0, 3 * y), sphere, red);
            }
        }

        Material floorMaterial = Material.checkerBoard(.5f, Color.of(.5f));
        addObject(StandardObjects.floorPlane(), floorMaterial);
    }
}
