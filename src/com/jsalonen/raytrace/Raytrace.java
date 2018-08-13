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

    Sphere sphere = new Sphere(Vec.of(0, 0, 1), .5f);

    Color resolveRayColor(Ray ray) {

        if (sphere.intersectsRay(ray)) {
            return Color.of(.7f, .7f, .7f);
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