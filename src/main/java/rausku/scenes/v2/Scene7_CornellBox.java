package rausku.scenes.v2;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.SceneObject;
import rausku.lighting.Color;
import rausku.lighting.DiskLight;
import rausku.material.Material;
import rausku.math.Vec;
import rausku.scenes.DefaultSceneDefinition;

import static rausku.geometry.StandardObjects.cube;
import static rausku.geometry.StandardObjects.sphere;
import static rausku.math.FloatMath.PI;
import static rausku.math.FloatMath.toRadians;
import static rausku.math.Matrix.*;

public class Scene7_CornellBox extends DefaultSceneDefinition {
    {
        setCamera(Camera.lookAt(
                Vec.point(0, 0f, 15),
                Vec.point(0, 0f, 0),
                500, 500,
                toRadians(30)));

//        addLight(new RectangleLight(translate(0, 2.99f, 0), Color.of(16f)));
        var translate = translate(0, 2.99f, 0);
        addLight(translate, new DiskLight(translate, Color.of(16f)));
//         addLight(new PointLight(Vec.point(0, 2.99f, 0), Color.of(2f)));

        Material red = Material.lambertian(Color.of(.8f, .2f, .2f));
        Material green = Material.lambertian(Color.of(.2f, .4f, .8f));
        Material white = Material.lambertian(Color.of(.7f));

        SceneObject object = cube();
        addObject(mul(translate(-1f, -1, -1.f), rotateY(PI / 12), scale(1, 2, 1)), object, white);
        addObject(mul(translate(+1, -2, +1), rotateY(-PI / 12)), sphere(), white);

        addObject(new HalfSpace(Vec.of(+1, 0, 0, 3)), red);
        addObject(new HalfSpace(Vec.of(-1, 0, 0, 3)), green);
        addObject(new HalfSpace(Vec.of(0, 0, +1, 3)), white);
//        addObject(new HalfSpace(Vec.of(0, 0, -1, 3)), matteWhite);
        addObject(new HalfSpace(Vec.of(0, -1, 0, 3)), white);
        addObject(new HalfSpace(Vec.of(0, 1, 0, 3)), white);
    }
}
