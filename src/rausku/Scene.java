package rausku;

import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.DirectionalLight;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Scene {

    private AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));
    private List<DirectionalLight> directionalLight = new ArrayList<>();

    private Camera camera = Camera.initialCamera();
    private List<Matrix> transforms = new ArrayList<>();
    private List<Matrix> inverseTransforms = new ArrayList<>();
    private List<SceneObject> objects = new ArrayList<>();

    protected void addObject(Matrix transform, SceneObject object) {
        getTransforms().add(transform);
        getInverseTransforms().add(transform.inverse());
        getObjects().add(object);
    }

    protected void addObject(SceneObject object) {
        getTransforms().add(Matrix.eye());
        getInverseTransforms().add(Matrix.eye());
        getObjects().add(object);
    }

    public Camera getCamera() {
        return camera;
    }

    protected void setCamera(Camera camera) {
        this.camera = camera;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public Collection<DirectionalLight> getLights() {
        if (directionalLight.isEmpty()) {
            return List.of(new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f)));
        }
        return directionalLight;
    }

    protected void addDirectionalLight(DirectionalLight directionalLight) {
        this.directionalLight.add(directionalLight);
    }

    public List<Matrix> getTransforms() {
        return transforms;
    }

    public List<Matrix> getInverseTransforms() {
        return inverseTransforms;
    }

    public List<SceneObject> getObjects() {
        return objects;
    }
}
