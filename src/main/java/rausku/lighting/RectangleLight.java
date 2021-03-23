package rausku.lighting;

import rausku.geometry.BoundingBox;
import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

import static rausku.math.FloatMath.abs;

public class RectangleLight implements LightSource, SceneObject {
    private final Matrix transform;
    private final Matrix inverse;
    private final Color color;

    public RectangleLight(Matrix transform, Color color) {
        this.transform = transform;
        this.inverse = transform.inverse();
        this.color = color;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        Vec sampledPoint = Vec.point(2 * s - 1, 0, 2 * t - 1);
        Vec worldLightPoint = transform.transform(sampledPoint);
        Ray ray = Ray.fromStartEnd(intercept.worldInterceptPoint, worldLightPoint);

        Vec toLight = worldLightPoint.sub(intercept.worldInterceptPoint);
        var squaredDistance = toLight.sqLen();
        return new Sample(color, ray, squaredDistance * .25f / abs(ray.direction.y));
    }

    @Override
    public boolean intercepts(Ray globalRay) {
        Ray localRay = inverse.transform(globalRay);
        float intercept = -localRay.origin.y / localRay.direction.y;
        Vec interceptPoint = localRay.apply(intercept);
        return -1 <= interceptPoint.x && interceptPoint.x <= 1 &&
                -1 <= interceptPoint.z && interceptPoint.z <= 1;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public float getIntensity(Vec interceptPoint) {
        return 1;
    }

    @Override
    public Vec getNormal(Intercept intercept) {
        return Vec.J.mul(-1);
    }

    @Override
    public Intercept getIntercept(Ray ray) {
        float intercept = -ray.origin.y / ray.direction.y;
        Vec interceptPoint = ray.apply(intercept);

        if (-1 <= interceptPoint.x && interceptPoint.x <= 1 &&
                -1 <= interceptPoint.z && interceptPoint.z <= 1) {
            return new Intercept(intercept, interceptPoint, null);
        } else {
            return Intercept.noIntercept();
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(-1, 1, 0, 0, -1, 1);
    }
}
