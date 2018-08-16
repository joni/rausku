package com.jsalonen.raytrace;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.*;

public class Raytrace {
    public static void main(String... args) {
        int pixelWidth = 640;
        int pixelHeight = 480;
        Canvas canvas = new Canvas(pixelWidth, pixelHeight, 1280, Vec.of(0, 0, 0), 1);

        Scene scene = new Scene();

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixelHeight; y++) {
            for (int x = 0; x < pixelWidth; x++) {
                Ray ray = canvas.getRayFromOriginToCanvas(x, y);

                Color color = scene.resolveRayColor(1, ray);

                image.setRGB(x, y, color.toIntRGB());
            }
        }


        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel label = new JLabel(new ImageIcon(image));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }
            });
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

class Scene {

    public static final double INTERCEPT_NEAR = 1e-3;
    List<SceneObject> objects;

    DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, .5f).normalize(), Color.of(1, 1, 1));
    AmbientLight ambientLight = new AmbientLight(Color.of(.5f, .5f, .5f));

    public Scene() {
        Color silver = Color.of(1f, 1f, 1f);
        Sphere sphere = new Sphere(Vec.of(-.5f, 0, 5), .5f, Material.metallic(silver, .9f));
        Sphere sphere2 = new Sphere(Vec.of(.0f, 0, 5.866f), .5f, Material.plastic(silver, .5f));
        Sphere sphere3 = new Sphere(Vec.of(.5f, 0, 5), .5f, Material.metallic(silver, .9f));
        Sphere sphere4 = new Sphere(Vec.of(.0f, .866f, 5.422f), .5f, Material.plastic(silver, .5f));
        HorizontalPlane plane = new HorizontalPlane(-.5f);

        objects = new ArrayList<>();
        for (int i = -3; i <= 3; i++) {
            for (int j = -3; j <= 3; j++) {
                objects.add(new Sphere(Vec.of(i, j, 10), .5f, Material.plastic(Color.of(.8f, .2f, .2f), .5f)));
            }
        }

//        objects.add(plane);
    }

    boolean interceptsRay(Ray ray) {
        return objects.stream().anyMatch(o -> o.getIntercept(ray) > INTERCEPT_NEAR);
    }

    Color resolveRayColor(float reflectiveness, Ray ray) {

        float closestIntercept = Float.POSITIVE_INFINITY;
        SceneObject closestObject = null;

        for (SceneObject object : objects) {
            float intercept = object.getIntercept(ray);
            if (intercept > INTERCEPT_NEAR && intercept < closestIntercept) {
                closestIntercept = intercept;
                closestObject = object;
            }
        }

        if (closestObject != null) {
            Vec interceptPoint = ray.apply(closestIntercept);
            return getColorFromObject(reflectiveness, interceptPoint, ray, closestObject);
        }

        // nothing hit
        if (directionalLight.direction.cos(ray.getDirection()) < -.99) {
            return Color.of(10f, 10f, 10f);
        }

        return Color.of(.1f, .1f, .1f);
    }

    private Color getColorFromObject(float reflectiveness, Vec interceptPoint, Ray ray, SceneObject sceneObject) {

        Vec normal = sceneObject.getNormal(interceptPoint).normalize();

        Material material = sceneObject.getMaterial();
        Color light = ambientLight.color.copy();

        float directionalLightEnergy = max(0, -normal.dot(directionalLight.direction));
        if (directionalLightEnergy > 0) {
            Ray lightRay = directionalLight.getRay(interceptPoint);
            if (!interceptsRay(lightRay)) {
                light.add(directionalLight.color.copy().mul(directionalLightEnergy));
            }
        }

        Color diffuseColor = material.getDiffuseColor().copy().mul(light);

        if (reflectiveness <= 1e-9 || material.getReflectiveness() <= 0) {
            return diffuseColor;
        } else {
            Ray reflected = ray.getReflected(normal, interceptPoint);
            Color reflectedLight = resolveRayColor(reflectiveness * material.getReflectiveness(), reflected);
            reflectedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor.add(reflectedLight);
            return diffuseColor;
        }
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

    public Ray getReflected(Vec normal, Vec interceptPoint) {
        return new Ray(interceptPoint, normal.reflected(direction));
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
        int r = min(255, (int) (256 * (1 - exp(-this.r))));
        int g = min(255, (int) (256 * (1 - exp(-this.g))));
        int b = min(255, (int) (256 * (1 - exp(-this.b))));
        return (0xff << 24) | (r << 16) | (g << 8) | b;
    }

    public Color mulAdd(float directionalLightEnergy, Color color, Color color1) {
        this.r = this.r * directionalLightEnergy * color.r + color1.r;
        this.g = this.g * directionalLightEnergy * color.g + color1.g;
        this.b = this.b * directionalLightEnergy * color.b + color1.b;
        return this;
    }

    public Color mul(Color color) {
        this.r *= color.r;
        this.g *= color.g;
        this.b *= color.b;
        return this;
    }

    public Color copy() {
        return new Color(r, g, b);
    }

    public Color mul(float directionalLightEnergy) {
        this.r *= directionalLightEnergy;
        this.g *= directionalLightEnergy;
        this.b *= directionalLightEnergy;
        return this;
    }

    public Color add(Color resolveRayColor) {
        this.r += resolveRayColor.r;
        this.g += resolveRayColor.g;
        this.b += resolveRayColor.b;
        return this;
    }
}

abstract class SceneObject {
    public abstract Vec getNormal(Vec interceptPoint);

    public abstract Material getMaterial();

    public abstract float getIntercept(Ray ray);
}

class Sphere extends SceneObject {
    Vec center;
    float radius;
    Material material;

    public Sphere(Vec center, float radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public boolean intersectsRay(Ray ray) {
        return getIntercept(ray) > 1e-6;
    }

    public float getIntercept(Ray ray) {
        Vec a = ray.getDirection();
        Vec b = ray.getOrigin();
        Vec c = this.center;
        float r = this.radius;

        Vec bMINUSc = b.copy().sub(c);

        float A = a.sqLen();
        float B = 2 * a.dot(bMINUSc);
        float C = bMINUSc.sqLen() - r * r;

        float determinant = B * B - 4 * A * C;
        if (determinant > 0) {
            float intercept = (-B - (float) sqrt(determinant)) / (2 * A);
            if (intercept > 0) {
                return intercept;
            }
            intercept = (-B + (float) sqrt(determinant)) / (2 * A);
            if (intercept > 0) {
                return intercept;
            }
        }
        return Float.NaN;
    }

    public Vec getNormal(Vec point) {
        return point.copy().sub(center).normalize();
    }

    public Material getMaterial() {
        return material;
    }
}

class HorizontalPlane extends SceneObject {
    float groundLevel;

    public HorizontalPlane(float groundLevel) {
        this.groundLevel = groundLevel;
    }

    public float getIntercept(Ray ray) {
        float intercept = (groundLevel - ray.getOrigin().y) / ray.getDirection().y;
        if (intercept > 0) {
            return intercept;
        } else {
            return Float.NaN;
        }
    }

    public Vec getNormal(Vec point) {
        return Vec.of(0, 1, 0);
    }

    @Override
    public Material getMaterial() {
        return Material.plastic(Color.of(.24f, .5f, .2f), .3f);
    }
}

class Material {
    Color diffuseColor;
    Color reflectiveColor;
    float reflectiveness;
    float transparency;
    float indexOfRefraction;

    public Material(Color diffuseColor) {
        this.diffuseColor = diffuseColor;
        this.reflectiveness = 0f;
    }

    public Material(Color reflectiveColor, float reflectiveness) {
        this.diffuseColor = Color.of(.1f, .1f, .1f);
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
    }

    public Material(Color diffuseColor, Color reflectiveColor, float reflectiveness) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
    }

    static Material plastic(Color color, float reflectiveness) {
        return new Material(color, color, reflectiveness);
    }

    static Material metallic(Color color, float reflectiveness) {
        return new Material(Color.of(.01f, .01f, .01f), color, reflectiveness);
    }

    public float getReflectiveness() {
        return reflectiveness;
    }

    public Color getDiffuseColor() {
        return diffuseColor;
    }

    public Color getReflectiveColor() {
        return reflectiveColor;
    }
}

class DirectionalLight {
    Vec direction;
    Color color;

    DirectionalLight(Vec direction, Color color) {
        this.direction = direction;
        this.color = color;
    }

    public Ray getRay(Vec origin) {
        return new Ray(origin, direction.copy().mul(-1));
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
        float len = (float) sqrt(sqLen());
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

    public Vec reflected(Vec v) {
        return mulAdd(-2 * this.dot(v) / this.sqLen(), this, v);
    }

    public float cos(Vec v) {
        return this.dot(v) / (float) sqrt(this.sqLen() * v.sqLen());
    }
}