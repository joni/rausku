package rausku;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public interface RenderStrategy {

    BufferedImage render(RayTracer rayTracer, Camera camera, Sampler sampler, IntConsumer progressMonitor);

    class TimedStrategyDecorator implements RenderStrategy {

        private final RenderStrategy strategy;

        TimedStrategyDecorator(RenderStrategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public BufferedImage render(RayTracer rayTracer, Camera camera, Sampler sampler, IntConsumer progressMonitor) {
            long start = System.nanoTime();
            BufferedImage image = strategy.render(rayTracer, camera, sampler, progressMonitor);
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
        public BufferedImage render(RayTracer rayTracer, Camera camera, Sampler sampler, IntConsumer progressMonitor) {
            int pixelWidth = camera.getPixelWidth();
            int pixelHeight = camera.getPixelHeight();

            BufferedImage image = sampler.createImage(camera);

            for (int y = 0; y < pixelHeight; y++) {
                for (int x = 0; x < pixelWidth; x++) {
                    sampler.sample(rayTracer, camera, image, x, y);
                }
                progressMonitor.accept(100 * (y + 1) / pixelHeight);
            }

            return image;
        }
    }

    class PerLineThreaded implements RenderStrategy {

        private final ExecutorService executorService;

        public PerLineThreaded() {
            int threads = Runtime.getRuntime().availableProcessors();
            executorService = Executors.newFixedThreadPool(threads);
        }

        @Override
        public BufferedImage render(RayTracer rayTracer, Camera camera, Sampler sampler, IntConsumer progressMonitor) {

            int pixelHeight = camera.getPixelHeight();
            BufferedImage image = sampler.createImage(camera);

            List<Callable<Integer>> rowCallables = new ArrayList<>();

            AtomicInteger rowCounter = new AtomicInteger(1);

            for (int y = 0; y < pixelHeight; y++) {
                final int finalY = y;
                rowCallables.add(() -> {
                    int pixelWidth = camera.getPixelWidth();
                    for (int x = 0; x < pixelWidth; x++) {
                        sampler.sample(rayTracer, camera, image, x, finalY);
                    }
                    int row = rowCounter.incrementAndGet();
                    progressMonitor.accept(100 * row / pixelHeight);
                    return finalY;
                });
            }

            try {
                List<Future<Integer>> futures = executorService.invokeAll(rowCallables);
                executorService.shutdown();

                futures.forEach(f -> {
                    try {
                        // Check for exceptions
                        f.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            } catch (InterruptedException e) {
                // rendering was interrupted - who would do such a thing!
                e.printStackTrace();
            }

            return image;
        }
    }

}
