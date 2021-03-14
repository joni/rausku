package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Group;
import rausku.geometry.SceneObject;
import rausku.lighting.LightSource;
import rausku.material.Material;
import rausku.math.Matrix;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DefaultSceneDefinition implements SceneDefinition {

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
        return lights;
    }

    protected void addLight(LightSource lightSource) {
        this.lights.add(lightSource);
    }

    @Override
    public List<SceneObjectInstance> getObjects() {
        return objects;
    }

    protected void addObject(Matrix transform, SceneObject object, Material material) {
        Matrix matrix = Matrix.mul(transformStack.peek(), transform);
        objects.add(new SceneObjectInstance(object, matrix, matrix.inverse(), material));
    }

    protected void addObject(SceneObject object, Material material) {
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
