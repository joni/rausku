package rausku.algorithm;

import rausku.math.FloatMath;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.tan;

public class Camera {

    private static final Camera INITIAL = new Camera(Matrix.translate(0, 0, 10), 256, 256, FloatMath.toRadians(45));

    private final Matrix cameraMatrix;
    private final int pixelHeight;
    private final int pixelWidth;
    private final Matrix projectionMatrix;

    public Camera(Matrix cameraMatrix, int imageWidth, int imageHeight, float angleOfView) {
        this.cameraMatrix = cameraMatrix;
        this.pixelWidth = imageWidth;
        this.pixelHeight = imageHeight;
        float scaleFactor = Math.min(imageWidth, imageHeight) / tan(angleOfView / 2);

        projectionMatrix = Matrix.of(
                +1, +0, +0, -pixelWidth / 2f,
                +0, -1, +0, pixelHeight / 2f,
                +0, +0, scaleFactor, 0,
                +0, +0, +0, scaleFactor
        );
    }

    public static Camera initialCamera() {
        return INITIAL;
    }

    public Ray getRayFromOriginToCanvas(float x, float y) {

        Vec imagePoint = Vec.point(x, y, -1);

        Vec origin = Vec.origin();
        Vec direction = projectionMatrix.transform(imagePoint)
                .toCanonical()
                .sub(origin);

        return cameraMatrix.transform(Ray.fromOriginDirection(origin, direction));
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }
}
