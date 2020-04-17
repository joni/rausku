package rausku;

import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface Sampler {

    void sample(Scene scene, Camera camera, BufferedImage image, int x, int y);

    default BufferedImage createImage(Camera camera) {
        int pixelWidth = camera.getPixelWidth();
        int pixelHeight = camera.getPixelHeight();

        return new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
    }

    class Naive implements Sampler {
        @Override
        public void sample(Scene scene, Camera camera, BufferedImage image, int x, int y) {
            Ray ray = camera.getRayFromOriginToCanvas(x, y);
            Color color = scene.resolveRayColor(1, ray);
            image.setRGB(x, y, color.toIntRGB());
        }
    }

    class GaussianRandomSubSampler implements Sampler {

        private final int samplesPerPixel;

        public GaussianRandomSubSampler(int samplesPerPixel) {
            this.samplesPerPixel = samplesPerPixel;
        }

        public void sample(Scene scene, Camera camera, BufferedImage image, int x, int y) {
            Random rnd = ThreadLocalRandom.current();

            Color[] colors = new Color[samplesPerPixel];
            for (int i = 0; i < samplesPerPixel; i++) {
                Ray ray = camera.getRayFromOriginToCanvas(x + (float) rnd.nextGaussian() / 2, y + (float) rnd.nextGaussian() / 2);
                colors[i] = scene.resolveRayColor(1, ray);
            }
            image.setRGB(x, y, Color.average(colors).toIntRGB());
        }
    }

    class UniformRandomSubSampler implements Sampler {

        private final int samplesPerPixel;

        public UniformRandomSubSampler(int samplesPerPixel) {
            this.samplesPerPixel = samplesPerPixel;
        }

        public void sample(Scene scene, Camera camera, BufferedImage image, int x, int y) {
            Random rnd = ThreadLocalRandom.current();

            Color[] colors = new Color[samplesPerPixel];
            for (int i = 0; i < samplesPerPixel; i++) {
                Ray ray = camera.getRayFromOriginToCanvas(x + rnd.nextFloat() - .5f, y + rnd.nextFloat() - .5f);
                colors[i] = scene.resolveRayColor(1, ray);
            }
            image.setRGB(x, y, Color.average(colors).toIntRGB());
        }
    }

    class SubSampler implements Sampler {

        @Override
        public void sample(Scene scene, Camera camera, BufferedImage image, int x, int y) {
            Color[] colors = {
                    getColor(scene, camera, x - .25f, y - .25f),
                    getColor(scene, camera, x + .25f, y - .25f),
                    getColor(scene, camera, x, y),
                    getColor(scene, camera, x - .25f, y + .25f),
                    getColor(scene, camera, x + .25f, y + .25f)
            };
            Color avg = Color.average(colors);
            image.setRGB(x, y, avg.toIntRGB());
        }

        private Color getColor(Scene scene, Camera camera, float x, float y) {
            Ray ray = camera.getRayFromOriginToCanvas(x, y);
            return scene.resolveRayColor(1, ray);
        }
    }
}
