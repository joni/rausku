package rausku.geometry;

import rausku.lighting.Color;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.List;

public class Polygon {

    public final List<Vertex> vertices;

    public final Vec v0;
    public final Vec v1;
    public final Vec v2;
    public final Vec side1;
    public final Vec side2;
    public final Vec normal;

    public Polygon(Vertex a, Vertex b, Vertex c) {
        this.vertices = List.of(a, b, c);
        this.v0 = a.position;
        this.v1 = b.position;
        this.v2 = c.position;
        this.side1 = v1.sub(v0);
        this.side2 = v2.sub(v0);
        this.normal = Vec.cross(this.side2, this.side1).normalize();
    }

    public Polygon(Vec v0, Vec v1, Vec v2) {
        this.vertices = null;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.side1 = v1.sub(v0);
        this.side2 = v2.sub(v0);
        this.normal = Vec.cross(this.side2, this.side1).normalize();
    }

    public float getIntercept(Ray ray) {
        // plane: n.(v-v0)=0 ray: v=direction*t+origin
        // n.(direction*t+origin-v0)=0
        // t = n.(v0-origin)/(n.direction)

        float nDotD = normal.dot(ray.direction);
        if (nDotD > -1e-2) {
            // back face
            return Float.NaN;
        }

        float planeIntercept = normal.dot(v0.sub(ray.origin)) / nDotD;

        if (planeIntercept < SceneObject.INTERCEPT_NEAR) {
            return Float.NaN;
        }

        // is the intercept inside the triangle?

        Vec interceptPoint = ray.apply(planeIntercept).sub(v0);

        double a = side1.sqLen();
        double b = side1.dot(side2);
        double c = side2.sqLen();
        double x = side1.dot(interceptPoint);
        double y = side2.dot(interceptPoint);

        double determinant = a * c - b * b;
        double v1coord = (c * x - b * y) / determinant;
        double v2coord = (a * y - b * x) / determinant;
        if (0 <= v1coord && v1coord < 1
                && 0 <= v2coord && v2coord < 1
                && v1coord + v2coord < 1) {
            return planeIntercept;
        } else {
            return Float.NaN;
        }
    }

    public Color getColor(Vec interceptPoint) {
        interceptPoint = interceptPoint.sub(v0);
        float a = side1.sqLen();
        float b = side1.dot(side2);
        float c = side2.sqLen();
        float x = side1.dot(interceptPoint);
        float y = side2.dot(interceptPoint);

        float determinant = a * c - b * b;
        float u = (c * x - b * y) / determinant;
        float v = (a * y - b * x) / determinant;

        return Color.of(1 - u - v, u, v);
    }

    public Vec getNormal(Vec interceptPoint) {
        if (vertices == null)
            return normal;

        interceptPoint = interceptPoint.sub(v0);

        float a = side1.sqLen();
        float b = side1.dot(side2);
        float c = side2.sqLen();
        float x = side1.dot(interceptPoint);
        float y = side2.dot(interceptPoint);

        float determinant = a * c - b * b;
        float u = (c * x - b * y) / determinant;
        float v = (a * y - b * x) / determinant;

        return Vec.mulAdd(
                1 - u - v, vertices.get(0).normal,
                u, vertices.get(1).normal,
                v, vertices.get(2).normal)
                .normalize();
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "normal=" + normal +
                '}';
    }
}
