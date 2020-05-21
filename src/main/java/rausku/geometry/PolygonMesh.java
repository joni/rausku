package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class PolygonMesh implements SceneObject {

    private final BSPTree bspTree;

    public PolygonMesh(List<Polygon> polygons) {
        bspTree = new BSPTree(polygons);
    }

    @Override
    public Vec getNormal(Ray ray, Intercept interceptPoint) {
        Polygon.InterceptInfo polygonIntercept = (Polygon.InterceptInfo) interceptPoint.info;
        return polygonIntercept.getNormal();
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        return bspTree.getIntercept(ray);
    }

    @Override
    public String toString() {
        return String.format("PolygonMesh{bspTree=%s}", bspTree);
    }
}
