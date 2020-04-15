package rausku;

import java.awt.image.BufferedImage;
import java.util.Random;

public interface Sampler {
    BufferedImage sample(Scene scene, Camera camera);

    class Naive implements Sampler {
        public BufferedImage sample(Scene scene, Camera camera) {
            int pixelWidth = camera.getPixelWidth();
            int pixelHeight = camera.getPixelHeight();

            BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < pixelHeight; y++) {
                for (int x = 0; x < pixelWidth; x++) {
                    Ray ray = camera.getRayFromOriginToCanvas(x, y);
                    Color color = scene.resolveRayColor(1, ray);
                    image.setRGB(x, y, color.toIntRGB());
                }
            }
            return image;
        }
    }

    class RandomSubsampler implements Sampler {

        private int samplesPerPixel;

        public RandomSubsampler(int samplesPerPixel) {
            this.samplesPerPixel = samplesPerPixel;
        }

        public BufferedImage sample(Scene scene, Camera camera) {
            int pixelWidth = camera.getPixelWidth();
            int pixelHeight = camera.getPixelHeight();

            Random rnd = new Random();

            BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < pixelHeight; y++) {
                for (int x = 0; x < pixelWidth; x++) {
                    Color[] colors = new Color[samplesPerPixel];
                    for (int i = 0; i < samplesPerPixel; i++) {
                        Ray ray = camera.getRayFromOriginToCanvas(x + rnd.nextFloat(), y + rnd.nextFloat());
                        colors[i] = scene.resolveRayColor(1, ray);
                    }
                    image.setRGB(x, y, Color.average(colors).toIntRGB());
                }
            }
            return image;
        }
    }

    class SubSampler implements Sampler {

        public BufferedImage sample(Scene scene, Camera camera) {
            int pixelWidth = camera.getPixelWidth();
            int pixelHeight = camera.getPixelHeight();

            BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);

            for (int y = 0; y < pixelHeight; y++) {
                for (int x = 0; x < pixelWidth; x++) {
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
            }
            return image;
        }

        private Color getColor(Scene scene, Camera camera, float x, float y) {
            Ray ray = camera.getRayFromOriginToCanvas(x, y);
            return scene.resolveRayColor(1, ray);
        }
    }
}
