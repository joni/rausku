package rausku.math;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static rausku.math.FloatMath.sqrt;
import static rausku.math.FloatMath.toRadians;

public class MatrixTest {

    Random rng = new Random(1234L);

    @Test
    public void testMul() {
        Matrix one = Matrix.eye();
        Matrix two = Matrix.diag(2);
        Matrix three = Matrix.diag(3);
        Matrix six = Matrix.diag(6);
        Matrix a = Matrix.random(rng);
        Matrix b = Matrix.random(rng);
        Matrix c = Matrix.random(rng);

        assertEquals(Matrix.mul(two, three), six);
        assertEquals(Matrix.mul(a, two), Matrix.mul(two, a));
        // approximately equal
        // assertEquals(Matrix.mul(Matrix.mul(a, b), c), Matrix.mul(a, Matrix.mul(b, c)));
    }

    @Test
    @Disabled
    public void testMulAssociative() {
        Matrix a = Matrix.random(rng);
        Matrix b = Matrix.random(rng);
        Matrix c = Matrix.random(rng);
        // approximately equal but not exactly, therefore this test is skipped
        assertEquals(Matrix.mul(Matrix.mul(a, b), c), Matrix.mul(a, Matrix.mul(b, c)));
    }

    @Test
    public void testMulVec() {
        Matrix eye = Matrix.eye();
        Vec v = Vec.of(1, 2, 3);

        assertEquals(eye.transform(v), v);
    }

    @Test
    public void testRotateX() {
        Matrix rotate30deg = Matrix.rotateX(toRadians(30));
        Vec x = Vec.of(1, 0, 0);
        Vec y = Vec.of(0, 1, 0);
        Vec z = Vec.point(0, 0, 1);

        assertEquals(rotate30deg.transform(x), x);
        assertEquals(rotate30deg.transform(y), Vec.of(0, sqrt(3) / 2, .5f));
        assertEquals(rotate30deg.transform(z), Vec.point(0, -.5f, sqrt(3) / 2));
    }

    @Test
    public void testRotateY() {
        Matrix rotate30deg = Matrix.rotateY(toRadians(30));
        Vec x = Vec.of(1, 0, 0);
        Vec y = Vec.of(0, 1, 0);
        Vec z = Vec.point(0, 0, 1);

        assertEquals(rotate30deg.transform(x), Vec.of(sqrt(3) / 2, 0, -.5f));
        assertEquals(rotate30deg.transform(y), y);
        assertEquals(rotate30deg.transform(z), Vec.point(.5f, 0, sqrt(3) / 2));
    }

    @Test
    public void testRotateZ() {
        Matrix rotate30deg = Matrix.rotateZ(toRadians(30));
        Vec x = Vec.of(1, 0, 0);
        Vec y = Vec.point(0, 1, 0);
        Vec z = Vec.of(0, 0, 1);

        assertEquals(rotate30deg.transform(x), Vec.of(sqrt(3) / 2, .5f, 0));
        assertEquals(rotate30deg.transform(y), Vec.point(-.5f, sqrt(3) / 2, 0));
        assertEquals(rotate30deg.transform(z), z);
    }

    @Test
    public void testRotate() {
        Matrix rotate120deg = Matrix.rotate(Vec.of(1, 1, 1), toRadians(120));
        Vec x = Vec.of(1, 0, 0);
        Vec y = Vec.point(0, 1, 0);
        Vec z = Vec.of(0, 0, 1);
        System.out.println(rotate120deg);

        assertVecEquals(rotate120deg.transform(x), Vec.of(0, 1, 0));
        assertVecEquals(rotate120deg.transform(y), Vec.point(0, 0, 1));
        assertVecEquals(rotate120deg.transform(z), Vec.point(1, 0, 0));
    }

    @Test
    public void testTranslate() {
        Matrix translate = Matrix.translate(3, 2, -5);

        Vec p = Vec.point(-5, 3, 2);
        Vec v = Vec.of(-5, 3, 2);

        assertEquals(translate.transform(v), v, "Translation does not change vectors");
        assertEquals(translate.transform(p), Vec.point(-2, 5, -3), "Translation changes points");
    }

    private void assertVecEquals(Vec actual, Vec expected) {
        assertEquals(actual.sub(expected).len(), 0, 1e-6f);
    }
}