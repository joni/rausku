package rausku.geometry;

import rausku.Color;
import rausku.Ray;
import rausku.Scene;
import rausku.math.Vec;

import java.util.List;

public class Polygon {

    public final List<Vertex> vertices;

    public final Vec v0;
    public final Vec v1;
    public final Vec v2;
    public final Vec normal;

    public Polygon(Vertex a, Vertex b, Vertex c) {
        this.vertices = List.of(a, b, c);
        this.v0 = a.position;
        this.v1 = b.position.sub(this.v0);
        this.v2 = c.position.sub(this.v0);
        this.normal = Vec.cross(this.v2, this.v1).normalize();
    }

    public Polygon(Vec v0, Vec v1, Vec v2) {
        this.vertices = null;
        this.v0 = v0;
        this.v1 = v1.sub(v0);
        this.v2 = v2.sub(v0);
        this.normal = Vec.cross(this.v2, this.v1).normalize();
    }

    public Polygon(List<Vec> vecs) {
        this(vecs.get(0), vecs.get(1), vecs.get(2));
    }

    public float getIntercept(Ray ray) {
        // plane: n.(v-v0)=0 ray: v=direction*t+origin
        // n.(direction*t+origin-v0)=0
        // t = n.(v0-origin)/(n.direction)

        float nDotD = normal.dot(ray.getDirection());
        if (nDotD > 0) {
            // back face
            return Float.NaN;
        }

        float planeIntercept = normal.dot(v0.sub(ray.getOrigin())) / nDotD;

        if (planeIntercept < Scene.INTERCEPT_NEAR) {
            return Float.NaN;
        }

        // is the intercept inside the triangle?

        Vec interceptPoint = ray.apply(planeIntercept).sub(v0);

        double a = v1.sqLen();
        double b = v1.dot(v2);
        double c = v2.sqLen();
        double x = v1.dot(interceptPoint);
        double y = v2.dot(interceptPoint);

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
        float a = v1.sqLen();
        float b = v1.dot(v2);
        float c = v2.sqLen();
        float x = v1.dot(interceptPoint);
        float y = v2.dot(interceptPoint);

        float determinant = a * c - b * b;
        float u = (c * x - b * y) / determinant;
        float v = (a * y - b * x) / determinant;

        return Color.of(1 - u - v, u, v);
    }

    public Vec getNormal(Vec interceptPoint) {
        if (vertices == null)
            return normal;

        interceptPoint = interceptPoint.sub(v0);

        float a = v1.sqLen();
        float b = v1.dot(v2);
        float c = v2.sqLen();
        float x = v1.dot(interceptPoint);
        float y = v2.dot(interceptPoint);

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
