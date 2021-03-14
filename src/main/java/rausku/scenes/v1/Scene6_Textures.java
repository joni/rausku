package rausku.scenes.v1;

import rausku.algorithm.Camera;
import rausku.geometry.HalfSpace;
import rausku.geometry.QuadraticForm;
import rausku.lighting.Color;
import rausku.material.Material;
import rausku.material.TextureMaterial;
import rausku.math.*;
import rausku.scenes.DefaultSceneDefinition;
import rausku.texture.CheckerBoardTexture;
import rausku.texture.GinghamTexture;
import rausku.texture.ImageTexture;
import rausku.texture.NoiseTexture;

import java.io.IOException;

import static rausku.math.FloatMath.PI;
import static rausku.math.FloatMath.toRadians;

public class Scene6_Textures extends DefaultSceneDefinition {
    {
        setCamera(Camera.createCamera(
                Vec.point(0, 0, 15),
                Vec.of(0, 0, -1),
                500, 500,
                toRadians(45)));

        QuadraticForm object = QuadraticForm.createSphere(Vec.origin(), 1f);

        Noise2D noise = new PerlinNoise2D();
        addObject(Matrix.translate(-5, +5, 0), object, new TextureMaterial(new NoiseTexture(noise)));
        addObject(Matrix.translate(-3, +5, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(2))));
        addObject(Matrix.translate(-1, +5, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(3))));
        addObject(Matrix.translate(+1, +5, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(4))));
        addObject(Matrix.translate(+3, +5, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(5))));
        addObject(Matrix.translate(+5, +5, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(6))));

        addObject(Matrix.translate(-5, +3, 0), object, new TextureMaterial(new GinghamTexture(.25f, Color.of(.25f, .75f, .25f))));
        addObject(Matrix.translate(-3, +3, 0), object, new TextureMaterial(new CheckerBoardTexture(.5f)));
        try {
            addObject(Matrix.translate(-1, +3, 0), object, new TextureMaterial(new ImageTexture("data/demo.png", PI, -PI, 0, PI)));
        } catch (IOException e) {
            e.printStackTrace();
        }
//        addObject(Matrix.translate(+1, +3, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(4))));
//        addObject(Matrix.translate(+3, +3, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(5))));
//        addObject(Matrix.translate(+5, +3, 0), object, new TextureMaterial(new NoiseTexture(noise.fractalNoise(6))));

        Material planeMaterial = new TextureMaterial(new NoiseTexture(new WorleyNoise2D(),
                Color.of(1, 1, .25f), Color.of(.75f, 0, 0)));
        addObject(new HalfSpace(Vec.of(0f, 0f, 1f, 1.0001f)), planeMaterial);
    }
}
