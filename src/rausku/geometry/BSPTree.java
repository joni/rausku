package rausku.geometry;

import rausku.math.Ray;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;

public class BSPTree {

    private final BoundingBox bbox;
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

        if (!bbox.testIntercept(ray)) {
            return Intercept.noIntercept();
        }

        // We need to find the cells the ray passes through
        List<Node> cellsToCheck = new ArrayList<>();

        float[] intercepts = bbox.getIntercepts(ray);

        // Check the tree recursively
        root.addCellsToCheck(ray, intercepts[0], intercepts[1], cellsToCheck);

        // Then we check the polygons in these nodes
        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept closestInterceptObj = Intercept.noIntercept();
        int polysChecked = 0;
        for (Node node : cellsToCheck) {
            for (Polygon polygon : node.ownPolys) {
                polysChecked++;
                Intercept intercept = polygon.getIntercept(ray);
                if (intercept.isValid() && intercept.intercept < closestIntercept) {
                    closestIntercept = intercept.intercept;
                    closestInterceptObj = intercept;
                }
            }
        }

        polygonStats.accept(polysChecked);

        return closestInterceptObj;
    }

    @Override
    public String toString() {
        return String.format("BSPTree{bbox=%s, root=%s}", bbox, root);
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
            DoubleSummaryStatistics stats = polygons.stream().mapToDouble(poly -> poly.v0.dot(normal)).summaryStatistics();
            // median could be better
            return ((float) stats.getAverage());
        }

        public Split doSplit(List<Polygon> polygons) {
            float splitAt = splitAt(polygons);
            Split split = new Split();
            split.splitAt = splitAt;

            for (Polygon polygon : polygons) {
                float dot = polygon.v0.dot(normal) - splitAt;
                float dot1 = polygon.v1.dot(normal) - splitAt;
                float dot2 = polygon.v2.dot(normal) - splitAt;

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

        public float getIntercept(Ray ray, float splitAt) {
            // n.(dt+o) = s <=> t = (s - n.o)/n.d
            return (splitAt - Vec.dot(normal, ray.origin)) / Vec.dot(normal, ray.direction);
        }
    }

    static class Split {
        float splitAt;
        List<Polygon> positive = new ArrayList<>();
        List<Polygon> negative = new ArrayList<>();

        int maxSize() {
            return Math.max(positive.size(), negative.size());
        }
    }

    static class Node {
        NodeType nodeType;
        float splitAt;
        boolean leaf;

        List<Polygon> ownPolys;

        Node positive;
        Node negative;

        public Node(NodeType nodeType, List<Polygon> polygons) {
            this.nodeType = nodeType;

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
                    this.splitAt = split.splitAt;
                    this.ownPolys = List.of();
                    NodeType nextSplit = nodeType.nextSplit();
                    this.positive = new Node(nextSplit, split.positive);
                    this.negative = new Node(nextSplit, split.negative);
                }
            }
        }

        @Override
        public String toString() {
            return String.format("[%s@%s count=%d, +:%s, -:%s]", nodeType, splitAt, ownPolys.size(), positive, negative);
        }

        public void addCellsToCheck(Ray ray, float tmin, float tmax, List<Node> cellsToCheck) {

            if (leaf) {
                // no subcells
                cellsToCheck.add(this);
                return;
            }

            // Find intersection of ray with the partition plane
            float intercept = nodeType.getIntercept(ray, splitAt);

            // Do we enter on the positive or negative side?
            boolean positiveSide = ray.apply(tmin).dot(nodeType.normal) > splitAt;

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
                first.addCellsToCheck(ray, tmin, intercept, cellsToCheck);
                second.addCellsToCheck(ray, intercept, tmax, cellsToCheck);
            } else {
                // Ray is completely to one side of the partition
                first.addCellsToCheck(ray, tmin, tmax, cellsToCheck);
            }
        }
    }
}
