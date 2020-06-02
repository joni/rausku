package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.material.TextureMaterial;
import rausku.math.*;
import rausku.texture.NoiseTexture;

import static rausku.math.FloatMath.toRadians;

public class Scene6_Textures extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 15),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        QuadraticForm object = QuadraticForm.createSphere(Vec.origin(), 1f);

        Noise2D noise = new PerlinNoise2D();
        addObject(Matrix.translate(-5, 0, 0), object, new TextureMaterial(new NoiseTexture(noise)));
        addObject(Matrix.translate(-3, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(2))));
        addObject(Matrix.translate(-1, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(3))));
        addObject(Matrix.translate(+1, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(4))));
        addObject(Matrix.translate(+3, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(5))));
        addObject(Matrix.translate(+5, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(6))));

        Material planeMaterial = new TextureMaterial(new NoiseTexture(new WorleyNoise2D(),
                Color.of(1, 1, .25f), Color.of(.75f, 0, 0)));
        addObject(new HalfSpace(Vec.of(0f, 0f, 1f, 1.0001f)), planeMaterial);
    }
}
