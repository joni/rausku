package rausku;

import rausku.algorithm.*;
import rausku.scenes.Scene;
import rausku.scenes.Scene7_Teapot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Raytrace {

    public static void main(String... args) {

        Scene scene = new Scene7_Teapot();

        RayTracer rayTracer = new RecursiveRayTracer(scene, new RecursiveRayTracer.Params());

        Camera camera = scene.getCamera();

        Sampler sampler = new Sampler.GaussianRandomSubSampler(64);
//        Sampler sampler = new Sampler.Naive();

        RenderStrategy renderer = new RenderStrategy.TimedStrategyDecorator(new RenderStrategy.PerLineThreaded());

        BufferedImage image = renderer.render(rayTracer, camera, sampler, i -> {
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
