package rausku.geometry;

import rausku.math.Matrix;

/**
 * Utility class to create standard objects. Standard objects are scaled to the unit cube.
 */
public class StandardObjects {
    private static final BoundingBox UNIT_BOX = new BoundingBox(-1, +1, -1, +1, -1, +1);

    private StandardObjects() {
    }

    public static SceneObject sphere() {
        return new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f), UNIT_BOX);
    }

    public static SceneObject cylinder() {
        return new QuadraticForm(Matrix.diag(1f, 0f, 1f, -1f), UNIT_BOX);
    }

    public static SceneObject cone() {
        return new QuadraticForm(Matrix.of(
                1f, 0, 0, 0,
                0, -.25f, 0, .25f,
                0, 0, 1f, 0,
                0, .25f, 0, -.25f
        ), UNIT_BOX);
    }

    public static SceneObject cube() {
        return new Cube();
    }

    public static SceneObject torus() {
        return new Torus(.66667f, .3333f);
    }

    /**
     * Creates a standard floor plane (floor at y = -1)
     *
     * @return a standard floor plane
     */
    public static SceneObject floorPlane() {
        return HalfSpace.horizontalPlane(-1f);
    }
}
