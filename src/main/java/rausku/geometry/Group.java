package rausku.geometry;

import rausku.material.Material;
import rausku.math.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private final List<Material> materials = new ArrayList<>();
    private final List<Matrix> transforms = new ArrayList<>();

    private List<Geometry> objects = new ArrayList<>();
    private BoundingBox bbox = BoundingBox.empty();

    public Group() {
    }

    public void addObject(Matrix transform, Geometry object, Material material) {
        transforms.add(transform);
        objects.add(object);
        materials.add(material);
        bbox = BoundingBox.union(bbox, object.getBoundingBox());
    }

    public void addObject(Geometry object, Material material) {
        addObject(Matrix.eye(), object, material);
    }

    public void addObject(Geometry object) {
        addObject(Matrix.eye(), object, null);
    }

    @Override
    public String toString() {
        return "Group{" +
                "objectList=" + objects +
                ", bbox=" + bbox +
                '}';
    }

    public int size() {
        return objects.size();
    }

    public Matrix getTransform(int index) {
        return transforms.get(index);
    }

    public Geometry getObject(int index) {
        return objects.get(index);
    }

    public Material getMaterial(int index) {
        return materials.get(index);
    }
}
