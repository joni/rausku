package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.LightSource;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Scene {

    private static final DirectionalLight DEFAULT_LIGHT = new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f));

    private AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));
    private List<LightSource> lights = new ArrayList<>();

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

    public Collection<LightSource> getLights() {
        if (lights.isEmpty()) {
            return List.of(DEFAULT_LIGHT);
        }
        return lights;
    }

    protected void addLight(LightSource lightSource) {
        this.lights.add(lightSource);
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
