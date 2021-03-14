package rausku;

import rausku.algorithm.RayTracer;
import rausku.algorithm.RecursiveRayTracer;
import rausku.algorithm.RenderStrategy;
import rausku.algorithm.Sampler;
import rausku.scenes.Scene;
import rausku.scenes.SceneDefinition;
import rausku.scenes.v1.Scene7_Teapot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Raytrace {

    public static void main(String... args) {

        SceneDefinition sceneDefinition = new Scene7_Teapot();

        Scene scene = new Scene(sceneDefinition);
        RayTracer rayTracer = new RecursiveRayTracer(scene, new RecursiveRayTracer.Params());

        Sampler sampler = new Sampler.GaussianRandomSubSampler(64);
//        Sampler sampler = new Sampler.Naive();

        RenderStrategy renderer = new RenderStrategy.TimedStrategyDecorator(new RenderStrategy.PerLineThreaded());

        BufferedImage image = renderer.render(rayTracer, sceneDefinition.getCamera(), sampler, i -> {
            System.out.printf("\r%d%% ", i);
            System.out.flush();
        });
        System.out.println();

        try {
            ImageIO.write(image, "PNG", new File(scene.getClass().getSimpleName() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

