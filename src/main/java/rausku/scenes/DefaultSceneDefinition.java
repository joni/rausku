package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Geometry;
import rausku.geometry.Group;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.LightSource;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultSceneDefinition implements SceneDefinition {

    private static final List<LightSource> DEFAULT_LIGHTS = List.of(
            new DirectionalLight(Vec.unit(1, -1, -.5f), Color.of(.8f, .8f, .7f)),
            new AmbientLight(Color.of(.2f, .25f, .3f)));

    private final List<LightSource> lights = new ArrayList<>();
    private final List<SceneObjectInstance> objects = new ArrayList<>();
    private final ArrayDeque<Matrix> transformStack = new ArrayDeque<>();
    private Camera camera = Camera.initialCamera();

    public DefaultSceneDefinition() {
        transformStack.push(Matrix.eye());
    }

    protected void pushTransform(Matrix transform) {
        transformStack.push(Matrix.mul(transformStack.getLast(), transform));
    }

    protected void popTransform() {
        transformStack.pop();
    }

    @Override
    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public Collection<LightSource> getLights() {
        if (lights.isEmpty()) return DEFAULT_LIGHTS;
        return lights;
    }

    protected void addLight(LightSource lightSource) {
        if (lightSource instanceof Geometry geometry) {
            this.addObject(geometry, Material.lambertian(Color.of(1f)));
        }
        this.lights.add(lightSource);
    }

    protected void addLight(Matrix transform, LightSource lightSource) {
        if (lightSource instanceof Geometry geometry) {
            this.addObject(transform, geometry, Material.lambertian(Color.of(1f)));
        }
        this.lights.add(lightSource);
    }

    @Override
    public List<SceneObjectInstance> getObjects() {
        return objects;
    }

    protected void addObject(Matrix transform, Geometry object, Material material) {
        Matrix objectToWorld = Matrix.mul(transformStack.peek(), transform);
        objects.add(new SceneObjectInstance(object, objectToWorld.inverse(), objectToWorld, material));
    }

    protected void addObject(Geometry object, Material material) {
        addObject(Matrix.eye(), object, material);
    }

    protected void addGroup(Matrix transform, Group group) {
        pushTransform(transform);
        for (int i = 0; i < group.size(); i++) {
            addObject(group.getTransform(i), group.getObject(i), group.getMaterial(i));
        }
        popTransform();
    }
}
