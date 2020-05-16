package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class PolygonMesh implements SceneObject {

    private final Material material;
    private final BSPTree bspTree;

    public PolygonMesh(List<Polygon> polygons, Material material) {
        this.material = material;
        bspTree = new BSPTree(polygons);
    }

    @Override
    public Vec getNormal(Ray ray, Intercept interceptPoint) {
        Polygon polygon = (Polygon) interceptPoint.info;
        return polygon.getNormal(interceptPoint.interceptPoint);
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        return bspTree.getIntercept(ray);
    }
}
