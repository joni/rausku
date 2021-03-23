package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.SceneObject;
import rausku.geometry.StandardObjects;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.math.FloatMath.toRadians;

public class Scene1_AmbientLightWithMatteObjects extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(2, 4, 8),
                Vec.of(-2, -4, -8),
                500, 500,
                toRadians(30)));

        addLight(new AmbientLight(Color.of(2f)));

        Material matteRed = Material.lambertian(Color.of(.4f, .1f, .1f));
        Material matteWhite = Material.lambertian(Color.of(.3f));
        SceneObject sphere = StandardObjects.sphere();
        SceneObject cube = StandardObjects.cube();

        addObject(sphere, matteRed);
        addObject(Matrix.translate(-2, 0, -2), cube, matteWhite);
        addObject(Matrix.translate(+2, 0, -2), cube, matteWhite);
        addObject(Matrix.translate(-2, 0, +2), cube, matteWhite);
        addObject(Matrix.translate(+2, 0, +2), cube, matteWhite);

        addObject(StandardObjects.floorPlane(), matteWhite);
    }
}
