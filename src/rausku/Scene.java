package rausku;

import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));
    protected DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f));

    protected Camera camera = Camera.initialCamera();
    List<Matrix> transforms = new ArrayList<>();
    List<Matrix> inverseTransforms = new ArrayList<>();
    List<SceneObject> objects = new ArrayList<>();

    protected void addObject(Matrix transform, SceneObject object) {
        transforms.add(transform);
        inverseTransforms.add(transform.inverse());
        objects.add(object);
    }

    protected void addObject(SceneObject object) {
        transforms.add(Matrix.eye());
        inverseTransforms.add(Matrix.eye());
        objects.add(object);
    }

    public Camera getCamera() {
        return camera;
    }

}
