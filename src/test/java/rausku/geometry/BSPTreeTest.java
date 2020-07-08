package rausku.geometry;

import org.junit.jupiter.api.Test;
import rausku.math.FloatMath;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BSPTreeTest {

    @Test
    public void canAddPolygonToTree() {
        BSPTree bspTree = new BSPTree(List.of(new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1))));

        Ray ray = Ray.fromStartEnd(Vec.origin(), Vec.point(1, 1, 1));

        Intercept intercept = bspTree.getIntercept(ray);
        assertTrue(intercept.isValid());
    }

    @Test
    public void testTorus() {
        List<Polygon> torus = createTorus(2, 1);
        BSPTree bspTree = new BSPTree(torus);

        BasicPolygonMesh polygonMesh = new BasicPolygonMesh(torus);

        Random rnd = new Random();

        for (int i = 0; i < 100; i++) {
            Ray ray = Ray.fromOriginDirection(Vec.point(-4, -2, -4), Vec.of(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()));
            Intercept expected = polygonMesh.getIntercept(ray);
            Intercept actual = bspTree.getIntercept(ray);
            if (expected.isValid()) {
                assertEquals(actual.intercept, expected.intercept, 1e-6f);
            } else {
                assertFalse(actual.isValid(), ray.toString());
            }
        }

        IntSummaryStatistics polygonStats = bspTree.getPolygonStats();
        System.out.printf("average %.2f%%", 100 * polygonStats.getAverage() / torus.size());
    }

    private static class BasicPolygonMesh {

        List<Polygon> polygons;

        BasicPolygonMesh(List<Polygon> polygons) {
            this.polygons = polygons;
        }

        Intercept getIntercept(Ray ray) {

            float closestIntercept = Float.POSITIVE_INFINITY;
            for (Polygon polygon : polygons) {
                Intercept intercept = polygon.getIntercept(ray);
                if (intercept.isValid() && intercept.intercept < closestIntercept) {
                    closestIntercept = intercept.intercept;
                }
            }
            return new Intercept(closestIntercept, ray.apply(closestIntercept), null);
        }
    }

    private List<Polygon> createTorus(float R, float r) {

        List<Polygon> polygons = new ArrayList<>();
        List<Vertex> previous = new ArrayList<>();

        int uResolution = 80;
        int vResolution = 40;

        for (int i = 0; i <= uResolution; i++) {
            List<Vertex> current = new ArrayList<>();
            float u = i * 2 * FloatMath.PI / uResolution;
            float cosU = FloatMath.cos(u);
            float sinU = FloatMath.sin(u);

            float xCenter = R * cosU;
            float yCenter = 0;
            float zCenter = R * sinU;

            Vec center = Vec.point(xCenter, yCenter, zCenter);

            for (int j = 0; j <= vResolution; j++) {

                float v = j * 2 * FloatMath.PI / vResolution;
                float cosV = FloatMath.cos(v);
                float sinV = FloatMath.sin(v);

                float x = sinV * cosU;
                float y = cosV;
                float z = sinV * sinU;

                Vec normal = Vec.of(x, y, z);
                Vec point = Vec.mulAdd(r, normal, center);

                Vertex vertex = Vertex.of(point, normal);

                if (i > 0 && j > 0) {
                    Vertex vec0 = previous.get(j - 1);
                    Vertex vec1 = previous.get(j);
                    Vertex vec2 = current.get(j - 1);

                    polygons.add(new Polygon(vertex, vec0, vec1));
                    polygons.add(new Polygon(vertex, vec2, vec0));
                }

                current.add(vertex);
            }
            previous = current;
        }
        return polygons;
    }
}