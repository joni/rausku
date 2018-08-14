package com.jsalonen.raytrace;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Raytrace {
    public static void main(String... args) {
        int pixelWidth = 256;
        int pixelHeight = 256;
        Canvas canvas = new Canvas(pixelWidth, pixelHeight, 64, Vec.of(0, 0, 0), 1);

        Scene scene = new Scene();

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixelHeight; y++) {
            for (int x = 0; x < pixelWidth; x++) {
                Ray ray = canvas.getRayFromOriginToCanvas(x, y);

                Color color = scene.resolveRayColor(ray);

                image.setRGB(x, y, color.toIntRGB());
            }
        }


        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel label = new JLabel(new ImageIcon(image));
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

class Scene {

    DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, 1).normalize(), Color.of(1, 1, 1));
    AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .2f, .2f));
    Sphere sphere = new Sphere(Vec.of(0, 0, 1), .5f);

    Color resolveRayColor(Ray ray) {

        if (sphere.intersectsRay(ray)) {
            float intercept = sphere.getIntercept(ray);
            if (Float.isFinite(intercept)) {
                Vec interceptPoint = ray.apply(intercept);
                Vec normal = sphere.getNormal(interceptPoint).normalize();

                float directionalLightEnergy = Math.max(0, -normal.dot(directionalLight.direction));

                Color sphereColor = Color.of(.7f, .7f, .7f);

                return sphereColor.mulAdd(directionalLightEnergy, directionalLight.color, ambientLight.color);
            }
        }

        Vec direction = ray.getDirection();

        if (direction.y < 0) {
            // ground hit
            return Color.of(.4f, .4f, 0);
        }

        // nothing hit
        return Color.of(0, 0, 0);
    }
}

class Canvas {

    float sceneWidth;
    float sceneHeight;
    float dotsPerUnit;
    Vec origin;
    float distance;

    int pixelWidth;
    int pixelHeight;

    public Canvas(int pixelWidth, int pixelHeight, float dotsPerUnit, Vec origin, float distance) {
        this.sceneWidth = pixelWidth / dotsPerUnit;
        this.sceneHeight = pixelHeight / dotsPerUnit;
        this.dotsPerUnit = dotsPerUnit;
        this.origin = origin;
        this.distance = distance;
        this.pixelWidth = pixelWidth;
        this.pixelHeight = pixelHeight;
    }

    public Ray getRayFromOriginToCanvas(int pixelX, int pixelY) {
        float canvasX = (pixelX - pixelWidth / 2) / dotsPerUnit;
        float canvasY = (pixelHeight / 2 - pixelY) / dotsPerUnit;
        float canvasZ = distance;
        Vec canvasPoint = Vec.of(canvasX, canvasY, canvasZ);
        return Ray.from(origin, canvasPoint);
    }
}

class Ray {

    private final Vec origin;
    private final Vec direction;

    public Ray(Vec origin, Vec direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public static Ray from(Vec origin, Vec canvasPoint) {
        return new Ray(origin, canvasPoint);
    }

    public Vec getDirection() {
        return direction;
    }

    public Vec getOrigin() {
        return origin;
    }

    public Vec apply(float t) {
        return Vec.mulAdd(t, direction, origin);
    }
}

class Color {
    float r;
    float g;
    float b;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static Color of(float r, float g, float b) {
        return new Color(r, g, b);
    }

    public int toIntRGB() {
        int r = Math.min(255, (int) (256 * this.r));
        int g = Math.min(255, (int) (256 * this.g));
        int b = Math.min(255, (int) (256 * this.b));
        return (0xff << 24) | (r << 16) | (g << 8) | b;
    }

    public Color mulAdd(float directionalLightEnergy, Color color, Color color1) {
        this.r = this.r * directionalLightEnergy * color.r + color1.r;
        this.g = this.g * directionalLightEnergy * color.g + color1.g;
        this.b = this.b * directionalLightEnergy * color.b + color1.b;
        return this;
    }
}

class Sphere {
    Vec center;
    float radius;

    public Sphere(Vec center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public boolean intersectsRay(Ray ray) {
        // ray = at+b
        // |at+b - c|^2 = (at+b-c).(at+b-c) = a.a t^2 + 2 a.(b-c) t + (b-c).(b-c) < r
        // has solution if 4 (a.(b-c))^2 - 4 a.a ((b-c).(b-c)-r) >= 0
        // (a.(b-c))^2 >= a.a ((b-c).(b-c)-r)
        Vec a = ray.getDirection();
        Vec b = ray.getOrigin();
        Vec c = this.center;
        float r = this.radius;

        Vec bMINUSc = b.copy().sub(c);

        return Math.pow(a.dot(bMINUSc), 2) >= a.sqLen() * (bMINUSc.sqLen() - r);
    }

    public float getIntercept(Ray ray) {
        Vec a = ray.getDirection();
        Vec b = ray.getOrigin();
        Vec c = this.center;
        float r = this.radius;

        Vec bMINUSc = b.copy().sub(c);

        float A = a.sqLen();
        float B = 2 * a.dot(bMINUSc);
        float C = bMINUSc.sqLen() - r;

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            float sol1 = (-B - (float) Math.sqrt(determinant)) / (2 * A);
            if (sol1 > 0) {
                return sol1;
            } else {
                return (-B + (float) Math.sqrt(determinant)) / (2 * A);
            }
        }
        return Float.NaN;
    }

    public Vec getNormal(Vec point) {
        return point.copy().sub(center);
    }
}

class DirectionalLight {
    Vec direction;
    Color color;

    DirectionalLight(Vec direction, Color color) {
        this.direction = direction;
        this.color = color;
    }
}

class AmbientLight {
    Color color;

    AmbientLight(Color color) {
        this.color = color;
    }
}

class Vec {

    float x;
    float y;
    float z;

    private Vec() {
        // initialze to 0
    }

    private Vec(Vec v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vec mulAdd(float t, Vec direction, Vec origin) {
        return new Vec(direction.x * t + origin.x,
                direction.y * t + origin.y,
                direction.z * t + origin.z);
    }

    public Vec add(Vec v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return this;
    }

    public Vec sub(Vec v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return this;
    }

    public Vec mul(float a) {
        x *= a;
        y *= a;
        z *= a;
        return this;
    }

    public Vec div(float a) {
        x /= a;
        y /= a;
        z /= a;
        return this;
    }

    public float sqLen() {
        return x * x + y * y + z * z;
    }

    public float dot(Vec v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vec normalize() {
        float len = (float) Math.sqrt(sqLen());
        return div(len);
    }

    public Vec copy() {
        return new Vec(this);
    }

    public static Vec of(float x, float y, float z) {
        return new Vec(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("Vec(%f, %f, %f)", x, y, z);
    }
}