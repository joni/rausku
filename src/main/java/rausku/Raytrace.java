package rausku;

import rausku.algorithm.MonteCarloRayTracer;
import rausku.algorithm.RayTracer;
import rausku.algorithm.RenderStrategy;
import rausku.algorithm.Sampler;
import rausku.scenes.Scene;
import rausku.scenes.SceneDefinition;
import rausku.scenes.v2.Scene7_CornellBox;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Raytrace {

    public static void main(String... args) {

        SceneDefinition sceneDefinition = new Scene7_CornellBox();

        Scene scene = new Scene(sceneDefinition);
        RayTracer rayTracer = new MonteCarloRayTracer(scene);
//        RayTracer rayTracer = new RecursiveRayTracer(scene, new RecursiveRayTracer.Params());

        Sampler sampler = new Sampler.GaussianRandomSubSampler(8);
//        Sampler sampler = new Sampler.Naive();

        RenderStrategy renderer = new RenderStrategy.TimedStrategyDecorator(new RenderStrategy.PerLineThreaded());

        BufferedImage image = renderer.render(rayTracer, sceneDefinition.getCamera(), sampler, i -> {
            System.out.printf("\r%d%% ", i);
            System.out.flush();
        });
        var fileName = sceneDefinition.getClass().getSimpleName() + ".png";
        System.out.printf("Writing result to %s%n", fileName);

        try (var stream = Files.newOutputStream(Path.of(fileName))) {
            ImageIO.write(image, "PNG", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

