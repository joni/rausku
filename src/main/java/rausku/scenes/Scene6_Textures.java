package rausku.scenes;

import rausku.algorithm.Camera;
import rausku.geometry.QuadraticForm;
import rausku.material.TextureMaterial;
import rausku.math.Matrix;
import rausku.math.Noise;
import rausku.math.PerlinNoise2D;
import rausku.math.Vec;
import rausku.texture.NoiseTexture;
import rausku.texture.TextureX;

import static rausku.math.FloatMath.toRadians;

public class Scene6_Textures extends Scene {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 15),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        QuadraticForm object = QuadraticForm.createSphere(Vec.origin(), 1f);
        addObject(Matrix.translate(-2, 2, 0), object, new TextureMaterial(new TextureX()));

        Noise noise = new PerlinNoise2D();
        addObject(Matrix.translate(-5, 0, 0), object, new TextureMaterial(new NoiseTexture(noise)));
        addObject(Matrix.translate(-3, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(1))));
        addObject(Matrix.translate(-1, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(2))));
        addObject(Matrix.translate(+1, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(3))));
        addObject(Matrix.translate(+3, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(4))));
        addObject(Matrix.translate(+5, 0, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(5))));


//        addObject(new BumpySphere(Vec.point(-2, 0, -5f), 2f), Material.plastic(Color.of(.8f, .2f, .2f), .9f));
    }
}
