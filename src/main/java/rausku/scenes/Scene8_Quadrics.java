package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.BoundingBox;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.lighting.DirectionalLight;
import rausku.material.Material;
import rausku.math.Matrix;
import rausku.math.Vec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static rausku.math.FloatMath.toRadians;

public class Scene8_Quadrics extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(7, 7, 7),
                Vec.of(-4, -5, -4),
                500, 500,
                toRadians(45)));

        addLight(new DirectionalLight(Vec.of(-1f, -2f, 0f).normalize(), Color.of(.7f, .5f, .3f)));
        addLight(new DirectionalLight(Vec.of(0f, -2f, -1f).normalize(), Color.of(.3f, .5f, .7f)));

        BoundingBox bbox = new BoundingBox(Vec.point(-1.1f, -1.1f, -1.1f), Vec.point(1.1f, 1.1f, 1.1f));

        List<Matrix> grid = new ArrayList<>();
        List<Material> materials = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                grid.add(Matrix.translate(i * 3, 0, j * 3));
                float r = .2f + .8f * (i + 1) / 2f;
                float g = .2f + .8f * (j + 1) / 2f;
                float b = Math.abs(1 - (r + g) / 2);
                materials.add(Material.plastic(Color.of(r, g, b), .1f));
            }
        }

        Iterator<Matrix> iter = grid.iterator();
        Iterator<Material> matIter = materials.iterator();

        // Hyperboloid of two sheets
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1f, -1f, 1f, +0.1f), bbox), matIter.next());

        // cone
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1f, -1f, 1f, 0f), bbox), matIter.next());

        // Hyperboloid of one sheet
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1f, -1f, 1f, -0.1f), bbox), matIter.next());

        // Cylinder (hyperbolic)
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1, 0, -1, -.1f), bbox), matIter.next());

        // Cylinder (parabolic)
        addObject(iter.next(), new QuadraticForm(Matrix.of(
                1, 0, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, .5f,
                0, 0, .5f, 0
        ), bbox), matIter.next());

        // cylinder
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1f, 0f, 1f, -1f), bbox), matIter.next());

        // Paraboloid (elliptic)
        addObject(iter.next(), new QuadraticForm(Matrix.of(
                1, 0, 0, 0,
                0, 0, 0, .5f,
                0, 0, 1, 0,
                0, .5f, 0, 0
        ), bbox), matIter.next());

        // Paraboloid (hyperbolic)
        addObject(iter.next(), new QuadraticForm(Matrix.of(
                1, 0, 0, 0,
                0, 0, 0, -.5f,
                0, 0, -1, 0,
                0, -.5f, 0, 0
        ), bbox), matIter.next());

        // Sphere
        addObject(iter.next(), new QuadraticForm(Matrix.diag(1f, 1f, 1f, -1f)), matIter.next());

        addObject(HalfSpace.horizontalPlane(-1.0001f), Material.checkerBoard(1));
    }
}
