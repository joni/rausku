package rausku.geometry;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import rausku.math.Ray;
import rausku.math.Vec;

public class PolygonBenchmark {

    @Benchmark
    public void intersectBenchmark(Blackhole blackhole) {
        Polygon polygon = new Polygon(Vec.point(1, 0, 0), Vec.point(0, 1, 0), Vec.point(0, 0, 1));
        Ray ray = Ray.fromStartEnd(Vec.origin(), Vec.point(1, 1, 1));

        for (int i = 0; i < 1000; i++) {
            Intercept intercept = polygon.getIntercept(ray);
            blackhole.consume(intercept);
        }
    }

}
