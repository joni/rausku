package rausku.math;

import java.util.concurrent.ThreadLocalRandom;

import static rausku.math.FloatMath.floor;
import static rausku.math.FloatMath.sqrt;

public class WorleyNoise2D implements Noise2D {
    int[] permutation = new int[256];
    float[][] pts = new float[256][2];

    public WorleyNoise2D() {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();

        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
            pts[i][0] = rnd.nextFloat();
            pts[i][1] = rnd.nextFloat();
        }
        shuffle(permutation, 0, 255);
    }


    private void shuffle(int[] permutation, int start, int end) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = end; i > start; i--) {
            int swap = rnd.nextInt(i);
            int tmp = permutation[swap];
            permutation[swap] = permutation[i];
            permutation[i] = tmp;
        }
    }

    float getPointDistance(int cellX, int cellY, float u, float v) {
        float[] pt = pts[cellY + permutation[cellX & 255] & 255];
        return FloatMath.hypot(pt[0] - u, pt[1] - v);
    }

    @Override
    public float getValue(float u, float v) {

        int cellU = floor(u);
        int cellV = floor(v);

        u = u - cellU;
        v = v - cellV;

        float minDistance = Float.POSITIVE_INFINITY;
        for (int i = -1; i <= +1; i++) {
            for (int j = -1; j <= +1; j++) {
                float pointDistance = getPointDistance(cellU + i, cellV + j, u - i, v - j);
                if (pointDistance < minDistance) {
                    minDistance = pointDistance;
                }
            }
        }

        return minDistance * sqrt(2) - 1;
    }
}
