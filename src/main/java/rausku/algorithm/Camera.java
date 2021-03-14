package rausku.algorithm;

import rausku.math.FloatMath;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import static rausku.math.FloatMath.tan;

public class Camera {

    private static final Camera INITIAL = createCamera(Vec.point(0, 0, 10), Vec.of(0, 0, -1), 256, 256, FloatMath.toRadians(45));

    final Matrix cameraMatrix;
    private final int pixelHeight;
    private final int pixelWidth;
    private final Matrix projectionMatrix;

    private Camera(Matrix cameraMatrix, int imageWidth, int imageHeight, float angleOfView) {
        this.cameraMatrix = cameraMatrix;
        this.pixelWidth = imageWidth;
        this.pixelHeight = imageHeight;
        float scaleFactor = Math.min(imageWidth, imageHeight) / (2 * tan(angleOfView / 2));

        projectionMatrix = Matrix.of(
                -1, +0, +0, pixelWidth / 2f,
                +0, -1, +0, pixelHeight / 2f,
                +0, +0, scaleFactor, 0,
                +0, +0, +0, scaleFactor
        );
    }

    public static Camera createCamera(Vec position, Vec lookDirection, int imageWidth, int imageHeight, float angleOfView) {
        return createCamera(position, lookDirection, Vec.of(0, 1, 0), imageWidth, imageHeight, angleOfView);
    }

    public static Camera lookAt(Vec position, Vec lookAtPoint, int imageWidth, int imageHeight, float angleOfView) {
        return createCamera(position, lookAtPoint.sub(position), Vec.of(0, 1, 0), imageWidth, imageHeight, angleOfView);
    }

    public static Camera createCamera(Vec position, Vec lookDirection, Vec up, int imageWidth, int imageHeight, float angleOfView) {
        Vec forward = lookDirection.normalize();
        Vec left = Vec.cross(up, forward).normalize();
        Vec realUp = Vec.cross(forward, left);
        Matrix matrix = Matrix.ofColumns(left, realUp, forward, position);
        return new Camera(matrix, imageWidth, imageHeight, angleOfView);
    }

    public static Camera initialCamera() {
        return INITIAL;
    }

    public Ray getRayFromOriginToCanvas(float x, float y) {

        Vec imagePoint = Vec.point(x, y, 1);

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
