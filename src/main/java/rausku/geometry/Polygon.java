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
    public final Vec normal;
    private final Vec U;
    private final Vec V;
    private float m00, m01, m02, m10, m11, m12;

    public Polygon(Vertex p0, Vertex p1, Vertex p2) {
        this.vertices = List.of(p0, p1, p2);
        this.v0 = p0.position();
        this.v1 = p1.position();
        this.v2 = p2.position();
        Vec side1 = v1.sub(v0);
        Vec side2 = v2.sub(v0);
        this.normal = Vec.cross(side2, side1).normalize();

        float a = side1.sqLen();
        float b = side1.dot(side2);
        float c = side2.sqLen();
        float determinant = a * c - b * b;
        Vec U = Vec.mulAdd(c, side1, -b, side2).div(determinant);
        Vec V = Vec.mulAdd(a, side2, -b, side1).div(determinant);

        m00 = U.x();
        m01 = U.y();
        m02 = U.z();
        m10 = V.x();
        m11 = V.y();
        m12 = V.z();

        this.U = U;
        this.V = V;
    }

    public Polygon(Vec v0, Vec v1, Vec v2) {
        this.vertices = null;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        Vec side1 = v1.sub(v0);
        Vec side2 = v2.sub(v0);
        this.normal = Vec.cross(side2, side1).normalize();

        float a = side1.sqLen();
        float b = side1.dot(side2);
        float c = side2.sqLen();
        float determinant = a * c - b * b;
        Vec U = Vec.mulAdd(c, side1, -b, side2).div(determinant);
        Vec V = Vec.mulAdd(a, side2, -b, side1).div(determinant);

        m00 = U.x();
        m01 = U.y();
        m02 = U.z();
        m10 = V.x();
        m11 = V.y();
        m12 = V.z();

        this.U = U;
        this.V = V;
    }

    public Intercept getIntercept(Ray ray) {
        // plane: n.(v-v0)=0 ray: v=d*t+o
        // n.(v*t+o-v0)=0
        // t = n.(v0-o)/(n.d)

        float nDotD = normal.dot(ray.direction);
        if (nDotD > -1e-2) {
            // back face culling
//            return Intercept.noIntercept();
        }

        Vec sub = ray.origin.sub(v0);
        float dot = -normal.dot(sub);

        if (Math.signum(dot) != Math.signum(nDotD)) {
            // intersection is behind the ray
            return Intercept.noIntercept();
        }

        float planeIntercept = dot / nDotD;

        // is the intercept inside the triangle?
        Vec interceptPoint = Vec.mulAdd(planeIntercept, ray.direction, sub);
        float u = m00 * interceptPoint.x() + m01 * interceptPoint.y() + m02 * interceptPoint.z();
        float v = m10 * interceptPoint.x() + m11 * interceptPoint.y() + m12 * interceptPoint.z();
        if (0 <= u && 0 <= v && u + v < 1) {
            return new Intercept(planeIntercept, ray.apply(planeIntercept), new InterceptInfo(u, v));
        } else {
            return Intercept.noIntercept();
        }
    }

    public Color getColor(Vec interceptPoint) {
        interceptPoint = interceptPoint.sub(v0);

        float u = m00 * interceptPoint.x() + m01 * interceptPoint.y() + m02 * interceptPoint.z();
        float v = m10 * interceptPoint.x() + m11 * interceptPoint.y() + m12 * interceptPoint.z();

        return Color.of(1 - u - v, u, v);
    }

    public Vec getCenter() {
        float oneThird = 1 / 3f;
        return Vec.mulAdd(oneThird, v0, oneThird, v1, oneThird, v2);
    }

    @Override
    public String toString() {
        return String.format("Polygon{normal=%s}", normal);
    }

    public class InterceptInfo {

        final float u;
        final float v;

        public InterceptInfo(float u, float v) {
            this.u = u;
            this.v = v;
        }

        public Vec getNormal() {
            if (vertices == null)
                return normal;

            return Vec.mulAdd(
                    1 - u - v, vertices.get(0).normal(),
                    u, vertices.get(1).normal(),
                    v, vertices.get(2).normal())
                    .normalize();
        }
    }
}
