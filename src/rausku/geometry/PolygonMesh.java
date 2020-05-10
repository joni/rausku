package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class PolygonMesh implements SceneObject {

    private final Material material;
    private List<Polygon> polygons;

    public PolygonMesh(List<Polygon> polygons, Material material) {
        this.polygons = polygons;
        this.material = material;
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
        float closestIntercept = Float.POSITIVE_INFINITY;
        Polygon closestPolygon = null;
        for (Polygon polygon : polygons) {
            float intercept = polygon.getIntercept(ray);
            if (Float.isFinite(intercept) && intercept > SceneObject.INTERCEPT_NEAR) {
                if (intercept < closestIntercept) {
                    closestIntercept = intercept;
                    closestPolygon = polygon;
                }
            }
        }
        return new Intercept(closestIntercept, ray.apply(closestIntercept), closestPolygon);
    }
}
