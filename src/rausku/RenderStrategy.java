package rausku;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public interface RenderStrategy {
    BufferedImage render(Scene scene, Camera camera, Sampler sampler);

    class TimedStrategyDecorator implements RenderStrategy {

        private RenderStrategy strategy;

        TimedStrategyDecorator(RenderStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public BufferedImage render(Scene scene, Camera camera, Sampler sampler) {
            long start = System.nanoTime();
            BufferedImage image = strategy.render(scene, camera, sampler);
            long nanoTime = System.nanoTime() - start;
            System.out.printf("Rendering took %d micros = %d millis = %d seconds%n",
                    TimeUnit.NANOSECONDS.toMicros(nanoTime),
                    TimeUnit.NANOSECONDS.toMillis(nanoTime),
                    TimeUnit.NANOSECONDS.toSeconds(nanoTime));
            return image;
        }
    }

    class SingleThreaded implements RenderStrategy {

        @Override
        public BufferedImage render(Scene scene, Camera camera, Sampler sampler) {
            int pixelWidth = camera.getPixelWidth();
            int pixelHeight = camera.getPixelHeight();

            BufferedImage image = sampler.createImage(camera);

            for (int y = 0; y < pixelHeight; y++) {
                for (int x = 0; x < pixelWidth; x++) {
                    sampler.sample(scene, camera, image, x, y);
                }
            }

            return image;
        }
    }

    class PerLineThreaded implements RenderStrategy {

        private final ExecutorService executorService;

        public PerLineThreaded() {
            executorService = Executors.newFixedThreadPool(8);
        }

        @Override
        public BufferedImage render(Scene scene, Camera camera, Sampler sampler) {

            int pixelHeight = camera.getPixelHeight();
            BufferedImage image = sampler.createImage(camera);

            List<Callable<Void>> rowCallables = new ArrayList<>();

            for (int y = 0; y < pixelHeight; y++) {
                final int finalY = y;
                rowCallables.add(() -> {
                    int pixelWidth = camera.getPixelWidth();
                    for (int x = 0; x < pixelWidth; x++) {
                        sampler.sample(scene, camera, image, x, finalY);
                    }
                    return null;
                });
            }

            try {
                executorService.invokeAll(rowCallables);
            } catch (InterruptedException e) {
                // rendering was interrupted - who would do such a thing!
                e.printStackTrace();
            }

            return image;
        }
    }

}
