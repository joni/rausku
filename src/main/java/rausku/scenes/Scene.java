package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.LightSource;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Scene {

    private static final DirectionalLight DEFAULT_LIGHT = new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f));

    private final AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));

    private Camera camera = Camera.initialCamera();
    private final List<LightSource> lights = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();
    private final List<Matrix> transforms = new ArrayList<>();
    private final List<Matrix> inverseTransforms = new ArrayList<>();
    private final List<SceneObject> objects = new ArrayList<>();

    protected void addObject(Matrix transform, SceneObject object, Material material) {
        transforms.add(transform);
        inverseTransforms.add(transform.inverse());
        objects.add(object);
        materials.add(material);
    }

    protected void addObject(SceneObject object, Material material) {
        addObject(Matrix.eye(), object, material);
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

    public List<SceneObject> getObjects() {
        return objects;
    }

    public Matrix getTransform(int index) {
        return transforms.get(index);
    }

    public Matrix getInverseTransform(int index) {
        return inverseTransforms.get(index);
    }

    public SceneObject getObject(int index) {
        return objects.get(index);
    }

    public Material getMaterial(int index) {
        return materials.get(index);
    }
}