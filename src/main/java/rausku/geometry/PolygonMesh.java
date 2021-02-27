package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class PolygonMesh implements SceneObject {

    private final String name;
    private final BSPTree bspTree;

    public PolygonMesh(List<Polygon> polygons) {
        this(null, polygons);
    }

    public PolygonMesh(String name, List<Polygon> polygons) {
        this.name = name;
        this.bspTree = new BSPTree(polygons);
    }

    @Override
    public Vec getNormal(Intercept interceptPoint) {
        Polygon.InterceptInfo polygonIntercept = (Polygon.InterceptInfo) interceptPoint.info;
        return polygonIntercept.getNormal();
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        return bspTree.getIntercept(ray);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return bspTree.bbox;
    }

    @Override
    public String toString() {
        return String.format("(mesh %s)", name);
    }
}
