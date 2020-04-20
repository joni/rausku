package rausku.geometry;

import rausku.material.Material;
import rausku.math.Ray;
import rausku.math.Vec;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Obj extends SceneObject {

    private final Material material;
    private List<Polygon> polygons;

    public Obj(List<Polygon> polygons, Material material) {
        this.polygons = polygons;
        this.material = material;
    }

    public Obj(String fileName, Material material) throws IOException {
        this.material = material;
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            this.polygons = new OBJLoader().parse(fileInputStream);
        }
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
    public Intercept getIntercept2(Ray ray) {
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

    @Override
    public float getIntercept(Ray ray) {
        return getIntercept2(ray).intercept;
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        return new float[0];
    }
}
