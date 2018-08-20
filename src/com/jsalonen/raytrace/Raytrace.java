package com.jsalonen.raytrace;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;

public class Raytrace {
    public static void main(String... args) {
        int pixelWidth = 768;
        int pixelHeight = 768;
        Canvas canvas = new Canvas(pixelWidth, pixelHeight, 1280, Vec.of(0, 0, -10), 1);

        Scene scene = new Scene5();

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage debugimage = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixelHeight; y++) {
            for (int x = 0; x < pixelWidth; x++) {
                Ray ray = canvas.getRayFromOriginToCanvas(x, y);

                Color color = scene.resolveRayColor(1, ray);

                image.setRGB(x, y, color.toIntRGB());
                Color debugcolor = scene.resolveRayColorDebug(1, ray, false);
                debugimage.setRGB(x, y, debugcolor.toIntRGB());
            }
        }


        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JLabel label = new JLabel(new ImageIcon(image));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Point point = e.getPoint();
                            Ray ray = canvas.getRayFromOriginToCanvas(point.x, point.y);

                            scene.resolveRayColorDebug(1, ray, true);

                            //System.exit(0);
                        }
                    });
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

class Scene1 extends Scene {

    public Scene1() {
        Color red = Color.of(.9f, .2f, .2f);
        Color green = Color.of(.2f, .9f, .2f);
        Color blue = Color.of(.2f, .2f, .9f);
        Sphere sphere = new Sphere(Vec.of(-.5f, 0, 5), .5f, Material.plastic(red, .5f));
        Sphere sphere2 = new Sphere(Vec.of(.0f, 0, 5.866f), .5f, Material.plastic(red, .5f));
        Sphere sphere3 = new Sphere(Vec.of(.5f, 0, 5), .5f, Material.plastic(blue, .5f));
        Sphere sphere4 = new Sphere(Vec.of(.0f, .8165f, 5.422f), .5f, Material.plastic(green, .5f));
        HorizontalPlane plane = new HorizontalPlane(-.5f);

        objects.add(sphere);
        objects.add(sphere2);
        objects.add(sphere3);
        objects.add(sphere4);

        objects.add(plane);
    }
}

class Scene2 extends Scene {

    public Scene2() {
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                objects.add(new Sphere(Vec.of(i, j, 10), .5f, Material.plastic(Color.of((i + 5) / 10f, (j + 5) / 10f, (10 - i - j) / 20f), .5f)));
            }
        }

        objects.add(new Sphere(Vec.of(0, 0, 5f), 1f, Material.glass()));
    }
}

class Scene3 extends Scene {
    public Scene3() {
        objects.add(new Cube(Material.plastic(Color.of(1, 1, 1), .8f)));
        objects.add(new Sphere(Vec.of(0, 0, 0f), 1.2f, Material.plastic(Color.of(.8f, .2f, .2f), .9f)));
        objects.add(new HorizontalPlane(-1));
    }
}

class Scene4 extends Scene {
    public Scene4() {
        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        objects.add(new Intersection(plastic,
                new Cube(plastic),
                new Sphere(Vec.of(0, 0, 0f), 1.2f, plastic)));
        objects.add(new HorizontalPlane(-1));
    }
}

class Scene5 extends Scene {
    public Scene5() {
        Material plastic = Material.plastic(Color.of(1, 1, 1), .8f);
        objects.add(new Subtraction(plastic,
                new Cube(plastic),
                new Sphere(Vec.of(0, 0, 0f), 1.4f, plastic)));
        objects.add(new HorizontalPlane(-1));
    }
}

abstract class Scene {

    public static final double INTERCEPT_NEAR = 1e-3;
    List<SceneObject> objects = new ArrayList<>();

    DirectionalLight directionalLight = new DirectionalLight(Vec.of(1, -1, .5f).normalize(), Color.of(1, 1, 1));
    AmbientLight ambientLight = new AmbientLight(Color.of(.1f, .1f, .1f));


    boolean interceptsRay(Ray ray) {
        return objects.stream().anyMatch(o -> o.getIntercept(ray) > INTERCEPT_NEAR);
    }

    Color resolveRayColorDebug(float reflectiveness, Ray ray, boolean debug) {
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
            if (debug) {
                System.out.printf("object %s\n", closestObject);
            }
            int hashCode = closestObject.hashCode();
            return Color.of((0xff & (hashCode >>> 16)) / 255f, (0xff & (hashCode >>> 8)) / 255f, (0xff & (hashCode)) / 255f);
        }

        return Color.of(0, 0, 0);
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

        return ambientLight.color;
    }

    private Color getColorFromObject(float reflectiveness, Vec interceptPoint, Ray ray, SceneObject sceneObject) {

        Vec normal = sceneObject.getNormal(ray, interceptPoint);

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

        if (reflectiveness <= 1e-6) {
            return diffuseColor;
        }

        if (material.getReflectiveness() > 0) {
            Ray reflected = ray.getReflected(normal, interceptPoint);
            Color reflectedLight = resolveRayColor(reflectiveness * material.getReflectiveness(), reflected);
            reflectedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor.add(reflectedLight);
        }

        if (material.hasRefraction()) {
            Ray refracted = ray.getRefracted(normal, interceptPoint, material.getIndexOfRefraction());
            Color refractedLight = resolveRayColor(reflectiveness, refracted);
//            refractedLight.mul(material.getReflectiveness()).mul(material.getReflectiveColor());
            diffuseColor.add(refractedLight);
        }

        return diffuseColor;
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
    private final float velocity;

    public Ray(Vec origin, Vec direction) {
        this(origin, direction.normalize(), 1);
    }

    public Ray(Vec origin, Vec direction, float velocity) {
        this.origin = origin;
        this.direction = direction;
        this.velocity = velocity;
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

    public Ray getRefracted(Vec normal, Vec interceptPoint, float indexOfRefraction) {
        return new Ray(interceptPoint, normal.refracted(direction, indexOfRefraction));
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
    public abstract Vec getNormal(Ray ray, Vec interceptPoint);

    public abstract Material getMaterial();

    public abstract float getIntercept(Ray ray);

    public abstract float[] getIntercepts(Ray ray);
}

class Intersection extends SceneObject {

    private final Material material;
    private SceneObject obj1;
    private SceneObject obj2;

    public Intersection(Material material, Cube cube, Sphere sphere) {
        this.material = material;
        obj1 = cube;
        obj2 = sphere;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public float getIntercept(Ray ray) {
        return max(obj1.getIntercept(ray), obj2.getIntercept(ray));
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        float[] intercepts = obj1.getIntercepts(ray);
        float[] intercepts1 = obj2.getIntercepts(ray);
        float[] allIntercepts = {max(intercepts[0], intercepts1[0]), min(intercepts[1], intercepts1[1])};
        return allIntercepts;
    }

    @Override
    public Vec getNormal(Ray ray, Vec interceptPoint) {
        float intercept1 = obj1.getIntercept(ray);
        float intercept2 = obj2.getIntercept(ray);
        float max = max(intercept1, intercept2);
        if (intercept1 == max) {
            return obj1.getNormal(ray, interceptPoint);
        } else if (intercept2 == max) {
            return obj2.getNormal(ray, interceptPoint);
        } else {
            // should not happen
            return null;
        }
    }
}

class Subtraction extends SceneObject {

    private final Material material;
    private final SceneObject obj1;
    private final SceneObject obj2;

    public Subtraction(Material material, Cube cube, Sphere sphere) {
        this.material = material;
        obj1 = cube;
        obj2 = sphere;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public float getIntercept(Ray ray) {
        float[] obj1Intercepts = obj1.getIntercepts(ray);
        float[] obj2Intercepts = obj2.getIntercepts(ray);

        if (!Float.isFinite(obj2Intercepts[0]) || obj1Intercepts[0] < obj2Intercepts[0]) {
            return obj1Intercepts[0];
        }
        if (obj2Intercepts[1] < obj1Intercepts[1]) {
            return obj2Intercepts[1];
        }
        return Float.NaN;

//        int count = 0;
//
//        int index1 = 0, index2=0;
//        while (count < 0 && index1 < obj1Intercepts.length && index2 < obj2Intercepts.length) {
//            if (obj1Intercepts[index1] < obj2Intercepts[index2]) {
//                return obj1Intercepts[0];
//            }
//        }

    }

    @Override
    public float[] getIntercepts(Ray ray) {
        return new float[0];
    }


    @Override
    public Vec getNormal(Ray ray, Vec interceptPoint) {
        float[] obj1Intercepts = obj1.getIntercepts(ray);
        float[] obj2Intercepts = obj2.getIntercepts(ray);

        if (!Float.isFinite(obj2Intercepts[0]) || obj1Intercepts[0] < obj2Intercepts[0]) {
            return obj1.getNormal(ray, interceptPoint);
        }
        if (obj2Intercepts[1] < obj1Intercepts[1]) {
            return obj2.getNormal(ray, interceptPoint).mul(-1);
        }
        return null;
    }

}

class Cube extends SceneObject {

    public static final double bounds = 1.000001;
    private final Material material;

    public Cube(Material material) {
        this.material = material;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Vec getNormal(Ray ray, Vec interceptPoint) {
        float absx = abs(interceptPoint.x);
        float absy = abs(interceptPoint.y);
        float absz = abs(interceptPoint.z);
        if (absx > absy && absx > absz) {
            return Vec.of(interceptPoint.x, 0, 0).normalize();
        }
        if (absy > absx && absy > absz) {
            return Vec.of(0, interceptPoint.y, 0).normalize();
        }
        if (absz > absx && absz > absy) {
            return Vec.of(0, 0, interceptPoint.z).normalize();
        }
        return interceptPoint.copy().normalize();
    }

    @Override
    public float[] getIntercepts(Ray ray) {
        float[] intercepts = {Float.NaN, Float.NaN};
        int index = 0;
        float[] possibleIntercepts = {
                (+1 - ray.getOrigin().x) / ray.getDirection().x,
                (-1 - ray.getOrigin().x) / ray.getDirection().x,
                (+1 - ray.getOrigin().y) / ray.getDirection().y,
                (-1 - ray.getOrigin().y) / ray.getDirection().y,
                (+1 - ray.getOrigin().z) / ray.getDirection().z,
                (-1 - ray.getOrigin().z) / ray.getDirection().z,
        };
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (float intercept : possibleIntercepts) {
            if (isOk(ray, intercept) && index < 2) {
                closestIntercept = intercept;
                intercepts[index++] = intercept;
            }
        }
        Arrays.sort(intercepts);
        return intercepts;
    }

    @Override
    public float getIntercept(Ray ray) {
        float[] intercepts = {
                (+1 - ray.getOrigin().x) / ray.getDirection().x,
                (-1 - ray.getOrigin().x) / ray.getDirection().x,
                (+1 - ray.getOrigin().y) / ray.getDirection().y,
                (-1 - ray.getOrigin().y) / ray.getDirection().y,
                (+1 - ray.getOrigin().z) / ray.getDirection().z,
                (-1 - ray.getOrigin().z) / ray.getDirection().z,
        };
        float closestIntercept = Float.POSITIVE_INFINITY;
        for (float intercept : intercepts) {
            if (isOk(ray, intercept) && intercept < closestIntercept) {
                closestIntercept = intercept;
            }
        }
        if (Float.isFinite(closestIntercept)) {
            return closestIntercept;
        } else {
            return Float.NaN;
        }
    }

    private boolean isOk(Ray ray, float intercept) {
        if (intercept < Scene.INTERCEPT_NEAR) {
            return false;
        }
        Vec interceptPoint = ray.apply(intercept);
        return abs(interceptPoint.x) < bounds && abs(interceptPoint.y) < bounds && abs(interceptPoint.z) < bounds;
    }
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

    @Override
    public float[] getIntercepts(Ray ray) {
        float[] floats = {Float.NaN, Float.NaN};

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
            floats = new float[]{(-B - (float) sqrt(determinant)) / (2 * A), (-B + (float) sqrt(determinant)) / (2 * A)};
        }
        return floats;
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
            if (intercept > Scene.INTERCEPT_NEAR) {
                return intercept;
            }
            intercept = (-B + (float) sqrt(determinant)) / (2 * A);
            if (intercept > Scene.INTERCEPT_NEAR) {
                return intercept;
            }
        }
        return Float.NaN;
    }

    public Vec getNormal(Ray ray, Vec point) {
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

    @Override
    public float[] getIntercepts(Ray ray) {
        return new float[]{getIntercept(ray)};
    }

    public float getIntercept(Ray ray) {
        float intercept = (groundLevel - ray.getOrigin().y) / ray.getDirection().y;
        if (intercept > 0) {
            return intercept;
        } else {
            return Float.NaN;
        }
    }

    public Vec getNormal(Ray ray, Vec point) {
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

    public Material(Color diffuseColor, Color reflectiveColor, float reflectiveness, float transparency, float indexOfRefraction) {
        this.diffuseColor = diffuseColor;
        this.reflectiveColor = reflectiveColor;
        this.reflectiveness = reflectiveness;
        this.transparency = transparency;
        this.indexOfRefraction = indexOfRefraction;
    }

    static Material plastic(Color color, float reflectiveness) {
        return new Material(color, color, reflectiveness);
    }

    static Material metallic(Color color, float reflectiveness) {
        return new Material(Color.of(.01f, .01f, .01f), color, reflectiveness);
    }

    static Material glass() {
        return new Material(Color.of(0.01f, .01f, .01f), Color.of(1f, 1f, 1f), 0f, .99f, 1.5f);
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

    public boolean hasRefraction() {
        return transparency > 0;
    }

    public float getIndexOfRefraction() {
        return indexOfRefraction;
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

    /**
     * Computes sU + tV
     */
    public static Vec mulAdd(float s, Vec u, float t, Vec v) {
        return new Vec(u.x * s + v.x * t,
                u.y * s + v.y * t,
                u.z * s + v.z * t);
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

    public Vec refracted(Vec v, float r) {
        float dot = this.dot(v);
        float s, t;
        if (dot > 0) {
            t = r;
            s = (float) (t * dot - sqrt(1 - pow(t, 2) * (1 - pow(dot, 2))));
        } else {
            t = 1 / r;
            s = (float) (t * dot + sqrt(1 - pow(t, 2) * (1 - pow(dot, 2))));
        }
        return mulAdd(-s, this, t, v);
    }
}