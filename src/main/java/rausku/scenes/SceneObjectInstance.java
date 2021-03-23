package rausku.scenes;

import rausku.geometry.SceneObject;
import rausku.material.Material;
import rausku.math.Matrix;

public class SceneObjectInstance {
    public final SceneObject object;
    public final Matrix worldToObject;
    public final Matrix objectToWorld;
    public final Material material;

    public SceneObjectInstance(SceneObject object, Matrix worldToObject, Matrix objectToWorld, Material material) {
        this.object = object;
        this.worldToObject = worldToObject;
        this.objectToWorld = objectToWorld;
        this.material = material;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
