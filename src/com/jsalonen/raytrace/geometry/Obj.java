package com.jsalonen.raytrace.geometry;

import com.jsalonen.raytrace.Color;
import com.jsalonen.raytrace.Material;
import com.jsalonen.raytrace.Ray;
import com.jsalonen.raytrace.math.Vec;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class Obj extends SceneObject {

    private List<Polygon> polygons;
    private Material metallic = Material.metallic(Color.of(.8f, .8f, .8f), .2f);
    private Material plastic = Material.plastic(Color.of(.8f, .8f, .8f), 0);

    public Obj(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public Obj(String fileName) throws IOException {
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
        return plastic;
    }

    @Override
    public Intercept getIntercept2(Ray ray) {
        float closestIntercept = Float.POSITIVE_INFINITY;
        Polygon closestPolygon = null;
        for (Polygon polygon : polygons) {
            float intercept = polygon.getIntercept(ray);
            if (Float.isFinite(intercept)) {
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
