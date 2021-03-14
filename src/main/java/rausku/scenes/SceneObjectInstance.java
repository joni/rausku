package rausku.scenes;

import rausku.geometry.SceneObject;
import rausku.material.Material;
import rausku.math.Matrix;

public class SceneObjectInstance {
    public final SceneObject object;
    public final Matrix transform;
    public final Matrix inverseTransform;
    public final Material material;

    public SceneObjectInstance(SceneObject object, Matrix transform, Matrix inverseTransform, Material material) {
        this.object = object;
        this.transform = transform;
        this.inverseTransform = inverseTransform;
        this.material = material;
    }
}
