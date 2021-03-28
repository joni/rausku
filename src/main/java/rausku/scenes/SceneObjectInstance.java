package rausku.scenes;

import rausku.geometry.Geometry;
import rausku.lighting.AreaLight;
import rausku.material.Material;
import rausku.math.Matrix;

public class SceneObjectInstance {
    public final Geometry object;
    public final Matrix worldToObject;
    public final Matrix objectToWorld;
    public final Material material;
    public final AreaLight areaLight;

    public SceneObjectInstance(Geometry object, Matrix worldToObject, Matrix objectToWorld, Material material) {
        this.object = object;
        this.worldToObject = worldToObject;
        this.objectToWorld = objectToWorld;
        this.material = material;
        this.areaLight = null;
    }

    public SceneObjectInstance(Geometry geometry, Matrix worldToObject, Matrix objectToWorld, Material material, AreaLight areaLight) {
        this.object = geometry;
        this.worldToObject = worldToObject;
        this.objectToWorld = objectToWorld;
        this.material = material;
        this.areaLight = areaLight;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
