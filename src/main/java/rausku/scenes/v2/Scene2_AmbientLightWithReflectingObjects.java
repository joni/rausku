package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.Geometry;
import rausku.geometry.StandardObjects;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene2_AmbientLightWithReflectingObjects extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(8, 8, 8),
                Vec.point(1, 0, 1),
                500, 500,
                toRadians(30)));

        addLight(new AmbientLight(Color.of(5f)));

        Material mirror = Material.mirror();
        Material matteRed = Material.lambertian(Color.of(.10f, .01f, .01f));
        Material matteWhite = Material.checkerBoard(.5f, Color.of(.1f));

        Geometry object = StandardObjects.sphere();

        addObject(object, matteRed);

        Matrix translate = Matrix.translate(0f, 0f, +2.5f);
        for (int i = 0; i < 6; i++) {
            Matrix rotateY = Matrix.rotateY(toRadians(60 * i));
            addObject(Matrix.mul(rotateY, translate), object, mirror);
        }

        addObject(StandardObjects.floorPlane(), matteWhite);
    }
}
