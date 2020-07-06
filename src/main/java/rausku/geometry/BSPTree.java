package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;

public class BSPTree {

    public final BoundingBox bbox;
    private Node root;
    private IntSummaryStatistics polygonStats = new IntSummaryStatistics();

    BSPTree(List<Polygon> polygons) {
        bbox = new BoundingBox.Builder().addPolygons(polygons).build();
        root = new Node(NodeType.X, polygons);
    }

    public IntSummaryStatistics getPolygonStats() {
        return polygonStats;
    }

    public Intercept getIntercept(Ray ray) {
        float[] intercepts = bbox.getIntercepts(ray);
        if (intercepts.length == 0) {
            return Intercept.noIntercept();
        }
        // Check the tree recursively
        return root.getIntercept(ray, intercepts[0], intercepts[1]);
    }

    @Override
    public String toString() {
        return String.format("BSPTree{bbox=%s}", bbox);
    }

    enum NodeType {
        X(Vec.of(1, 0, 0)),
        Y(Vec.of(0, 1, 0)),
        Z(Vec.of(0, 0, 1));

        private Vec normal;

        NodeType(Vec normal) {
            this.normal = normal;
        }

        public float splitAt(List<Polygon> polygons) {
            DoubleSummaryStatistics stats = polygons.stream().mapToDouble(poly -> poly.getCenter().dot(normal)).summaryStatistics();
            // median could be better
            return ((float) stats.getAverage());
        }

        public Split doSplit(List<Polygon> polygons) {
            float splitAt = splitAt(polygons);
            Split split = new Split();
            Vec splitVector = Vec.of(normal.x, normal.y, normal.z, -splitAt);
            split.splitVector = splitVector;

            for (Polygon polygon : polygons) {
                float dot = polygon.v0.dot(splitVector);
                float dot1 = polygon.v1.dot(splitVector);
                float dot2 = polygon.v2.dot(splitVector);

                if (dot > 0 && dot1 > 0 && dot2 > 0) {
                    split.positive.add(polygon);
                } else if (dot < 0 && dot1 < 0 && dot2 < 0) {
                    split.negative.add(polygon);
                } else {
                    split.negative.add(polygon);
                    split.positive.add(polygon);
                }
            }

            return split;
        }

        public NodeType nextSplit() {
            return switch (this) {
                case X -> Y;
                case Y -> Z;
                case Z -> X;
            };
        }
    }

    static class Split {
        Vec splitVector;
        List<Polygon> positive = new ArrayList<>();
        List<Polygon> negative = new ArrayList<>();

        int maxSize() {
            return Math.max(positive.size(), negative.size());
        }
    }

    static class Node {
        Vec splitVector;
        boolean leaf;

        List<Polygon> ownPolys;

        Node positive;
        Node negative;

        public Node(NodeType nodeType, List<Polygon> polygons) {

            if (polygons.size() <= 5) {
                this.leaf = true;
                this.ownPolys = polygons;
            } else {
                Split split = nodeType.doSplit(polygons);
                if (split.maxSize() >= polygons.size()) {
                    this.leaf = true;
                    this.ownPolys = polygons;
                } else {
                    this.leaf = false;
                    this.splitVector = split.splitVector;
                    this.ownPolys = List.of();
                    NodeType nextSplit = nodeType.nextSplit();
                    this.positive = new Node(nextSplit, split.positive);
                    this.negative = new Node(nextSplit, split.negative);
                }
            }
        }

        private float getPlaneIntercept(Ray ray) {
            // n.(dt+o) = 0 <=> t = -n.o/n.d
            return -Vec.dot(splitVector, ray.origin) / Vec.dot(splitVector, ray.direction);
        }

        public Intercept getIntercept(Ray ray, float tmin, float tmax) {
            if (leaf) {
                Intercept closestIntercept = Intercept.noIntercept();

                for (Polygon polygon : ownPolys) {
                    Intercept intercept = polygon.getIntercept(ray);
                    if (intercept.isValid() && tmin < intercept.intercept && intercept.intercept < tmax && intercept.intercept < closestIntercept.intercept) {
                        closestIntercept = intercept;
                    }
                }

                if (closestIntercept.isValid()) {
                    return closestIntercept;
                } else {
                    return Intercept.noIntercept();
                }
            }

            // Find intersection of ray with the partition plane
            float intercept = getPlaneIntercept(ray);

            // Do we enter on the positive or negative side?
            boolean positiveSide = Vec.dot(splitVector, ray.apply(tmin)) > 0;

            Node first, second;
            if (positiveSide) {
                first = positive;
                second = negative;
            } else {
                first = negative;
                second = positive;
            }

            if (tmin <= intercept && intercept <= tmax) {
                // Intercept with partition plane is within this cell. Both children have to be tested
                Intercept firstIntercept = first.getIntercept(ray, tmin, intercept);
                if (firstIntercept.isValid()) {
                    return firstIntercept;
                } else {
                    return second.getIntercept(ray, intercept, tmax);
                }
            } else {
                // Ray is completely to one side of the partition
                return first.getIntercept(ray, tmin, tmax);
            }
        }

        @Override
        public String toString() {
            if (leaf) {
                return String.format("[count=%d]", ownPolys.size());
            } else {
                return String.format("[%s +:%s, -:%s]", splitVector, positive, negative);
            }
        }
    }
}
