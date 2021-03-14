package rausku.lighting;

import rausku.math.FloatMath;
import rausku.math.Rand;
import rausku.math.Ray;
import rausku.math.Vec;
import rausku.scenes.SceneIntercept;

/**
 * Models a circular (or, in special case, point) light source "infinitely" far away in the sky dome. This can be used
 * to model the light from the sun for example.
 */
public class DirectionalLight implements LightSource {
    /**
     * Direction towards the light source
     */
    public final Vec towardsLight;

    /**
     * Spherical angle the source occupies. For example, 2*PI = hemisphere
     */
    public final float angle;

    /**
     * Radiance and spectrum of the light originating from this source
     */
    public final Color color;

    private final Vec perp1, perp2;

    public DirectionalLight(Vec direction, float angle, Color color) {
        this.towardsLight = direction.normalize().mul(-1);
        this.angle = angle;
        this.color = color;

        perp1 = towardsLight.perpendicular().normalize();
        perp2 = Vec.cross(towardsLight, perp1).normalize();
    }

    public DirectionalLight(Vec direction, Color color) {
        this.towardsLight = direction.normalize().mul(-1);
        this.angle = 0;
        this.color = color;

        perp1 = perp2 = null;
    }

    @Override
    public Sample sample(SceneIntercept intercept, float s, float t) {
        return new Sample(color, sampleRay(intercept, s, t), angle == 0 ? 1 : 1 / angle);
    }

    private Ray sampleRay(SceneIntercept intercept, float s, float t) {
        if (angle == 0) {
            return Ray.fromOriginDirection(intercept.worldInterceptPoint, towardsLight);
        } else {
            Vec cone = Rand.uniformCone(s, t, angle);
            Vec direction = Vec.mulAdd(cone.x, perp1, cone.y, towardsLight, cone.z, perp2);
            return Ray.fromOriginDirection(intercept.worldInterceptPoint, direction);
        }
    }

    public Vec getDirection() {
        return towardsLight;
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
    public boolean intercepts(Ray ray) {
        return Vec.cos(towardsLight, ray.direction) > FloatMath.cos(angle / 4);
    }
}
