package rausku.math;

import java.util.concurrent.ThreadLocalRandom;

import static rausku.math.FloatMath.floor;

public class PerlinNoise2D implements Noise {

    public static final int SIZE = 8;
    private final int[] permutation;

    public PerlinNoise2D() {
        this.permutation = new int[SIZE * 2];
        for (int i = 0; i < SIZE; i++) {
            permutation[i] = i;
        }
        shuffle(permutation, 0, SIZE);
        System.arraycopy(permutation, 0, permutation, 8, SIZE);
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

    int index(int u, int v) {
        return permutation[permutation[u] + v] & (SIZE - 1);
    }

    float getGradient(int U, int V, float u, float v) {
        int index = index(U, V);
        return getGradient(u, v, index);
    }

    float getGradient(float u, float v, int index) {
        int uf = getF(index + 2);
        int vf = getF(index);
        return uf * u + vf * v;
    }

    private int getF(int index) {
        if ((index & 3) == 0) {
            return 0;
        }
        int fourthBit = index & 4; // 0 or 4
        int dividedByTwo = fourthBit >> 1; // 0 or 2
        return 1 - dividedByTwo; // -1 or +1
    }

    @Override
    public float getValue(float u, float v) {
        int u0 = floor(u);
        int v0 = floor(v);
        float u_ = u - u0;
        float v_ = v - v0;

        int latticeU0 = u0 & (SIZE - 1);
        int latticeV0 = v0 & (SIZE - 1);
        int latticeU1 = latticeU0 + 1;
        int latticeV1 = latticeV0 + 1;

        float n0 = getGradient(latticeU0, latticeV0, u_, v_);
        float n1 = getGradient(latticeU1, latticeV0, u_ - 1, v_);
        float n2 = getGradient(latticeU0, latticeV1, u_, v_ - 1);
        float n3 = getGradient(latticeU1, latticeV1, u_ - 1, v_ - 1);

        float c0 = FloatMath.smoothstep5(n0, n1, u_);
        float c1 = FloatMath.smoothstep5(n2, n3, u_);
        float c = FloatMath.smoothstep5(c0, c1, v_);

        return c;
    }
}
