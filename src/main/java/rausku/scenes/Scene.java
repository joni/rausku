package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.Group;
import rausku.geometry.Intercept;
import rausku.geometry.SceneObject;
import rausku.lighting.AmbientLight;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.lighting.LightSource;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Ray;
import rausku.math.Vec;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Scene {

    private static final DirectionalLight DEFAULT_LIGHT = new DirectionalLight(Vec.of(1, -1, -.5f).normalize(), Color.of(.8f, .8f, .7f));

    private final AmbientLight ambientLight = new AmbientLight(Color.of(.2f, .25f, .3f));

    private Camera camera = Camera.initialCamera();
    private final List<LightSource> lights = new ArrayList<>();
    private final List<Material> materials = new ArrayList<>();
    private final List<Matrix> transforms = new ArrayList<>();
    private final List<Matrix> inverseTransforms = new ArrayList<>();
    private final List<SceneObject> objects = new ArrayList<>();

    private final ArrayDeque<Matrix> transformStack = new ArrayDeque<>();
    private boolean debug = false;

    public Scene() {
        transformStack.push(Matrix.eye());
    }

    protected void push(Matrix transform) {
        transformStack.push(Matrix.mul(transformStack.getLast(), transform));
    }

    protected void pop() {
        transformStack.pop();
    }

    protected void addGroup(Matrix transform, Group group) {
        push(transform);
        for (int i = 0; i < group.size(); i++) {
            addObject(group.getTransform(i), group.getObject(i), group.getMaterial(i));
        }
        pop();
    }

    protected void addObject(Matrix transform, SceneObject object, Material material) {
        Matrix matrix = Matrix.mul(transformStack.peek(), transform);
        transforms.add(matrix);
        inverseTransforms.add(matrix.inverse());
        objects.add(object);
        materials.add(material);
    }

    protected void addObject(SceneObject object, Material material) {
        addObject(Matrix.eye(), object, material);
    }

    public Camera getCamera() {
        return camera;
    }

    protected void setCamera(Camera camera) {
        this.camera = camera;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public Collection<LightSource> getLights() {
        if (lights.isEmpty()) {
            return List.of(DEFAULT_LIGHT);
        }
        return lights;
    }

    protected void addLight(LightSource lightSource) {
        this.lights.add(lightSource);
    }

    public List<SceneObject> getObjects() {
        return objects;
    }

    public Matrix getTransform(int index) {
        return transforms.get(index);
    }

    public Matrix getInverseTransform(int index) {
        return inverseTransforms.get(index);
    }

    public SceneObject getObject(int index) {
        return objects.get(index);
    }

    public Material getMaterial(int index) {
        return materials.get(index);
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean interceptsRay(Ray ray) {
        List<SceneObject> objects = this.objects;
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = inverseTransforms.get(i);
            Ray transform1 = transform.transform(ray);
            Intercept intercept2 = object.getIntercept(transform1);
            float intercept = intercept2.intercept;
            if (Float.isFinite(intercept) && intercept > 0 && intercept < ray.bound) {
                if (debug) {
                    ray.addDebug(transform1);
                    ray.addDebug(String.format("intercept %d, %s", i, intercept2));
                }
                return true;
            }
        }
        if (debug) {
            ray.addDebug("no intercept");
        }
        return false;
    }

    public SceneIntercept getIntercept(Ray ray) {
        float closestIntercept = Float.POSITIVE_INFINITY;
        Intercept intercept = Intercept.noIntercept();
        int interceptIndex = -1;

        List<SceneObject> objects = getObjects();
        for (int i = 0; i < objects.size(); i++) {
            SceneObject object = objects.get(i);
            Matrix transform = inverseTransforms.get(i);
            Ray transformed = transform.transform(ray);
            Intercept objectIntercept = object.getIntercept(transformed);
            float interceptValue = objectIntercept.intercept;
            if (interceptValue > SceneObject.INTERCEPT_NEAR && interceptValue < closestIntercept) {
                closestIntercept = interceptValue;
                intercept = objectIntercept;
                interceptIndex = i;
            }
        }
        return new SceneIntercept(interceptIndex, ray.apply(intercept.intercept), intercept);
    }
}
