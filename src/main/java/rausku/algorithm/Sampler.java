package rausku.algorithm;

import rausku.lighting.Color;
import rausku.math.Ray;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.WritableRaster;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public interface Sampler {

    void sample(RayTracer rayTracer, Camera camera, BufferedImage image, int x, int y);

    default BufferedImage createImage(Camera camera) {
        int pixelWidth = camera.getPixelWidth();
        int pixelHeight = camera.getPixelHeight();

        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
//        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);

        DirectColorModel colorModel = new DirectColorModel(colorSpace, 32,
                0x00ff0000,
                0x0000ff00,
                0x000000ff,
                0xff000000,
                false,
                DataBuffer.TYPE_INT);

        WritableRaster writableRaster = colorModel.createCompatibleWritableRaster(pixelWidth, pixelHeight);

        return new BufferedImage(colorModel, writableRaster, false, null);
    }

    class Naive implements Sampler {
        @Override
        public void sample(RayTracer rayTracer, Camera camera, BufferedImage image, int x, int y) {
            Ray ray = camera.getRayFromOriginToCanvas(x, y);
            Color color = rayTracer.resolveRayColor(ray);
            image.setRGB(x, y, color.toIntRGB());
        }
    }

    class GaussianRandomSubSampler implements Sampler {

        private final int samplesPerPixel;

        public GaussianRandomSubSampler(int samplesPerPixel) {
            this.samplesPerPixel = samplesPerPixel;
        }

        public void sample(RayTracer rayTracer, Camera camera, BufferedImage image, int x, int y) {
            Random rnd = ThreadLocalRandom.current();

            Color[] colors = new Color[samplesPerPixel];
            for (int i = 0; i < samplesPerPixel; i++) {
                Ray ray = camera.getRayFromOriginToCanvas(x + (float) rnd.nextGaussian() / 2, y + (float) rnd.nextGaussian() / 2);
                colors[i] = rayTracer.resolveRayColor(ray);
            }
            image.setRGB(x, y, Color.average(colors).toIntRGB());
        }
    }

    class UniformRandomSubSampler implements Sampler {

        private final int samplesPerPixel;

        public UniformRandomSubSampler(int samplesPerPixel) {
            this.samplesPerPixel = samplesPerPixel;
        }

        public void sample(RayTracer rayTracer, Camera camera, BufferedImage image, int x, int y) {
            Random rnd = ThreadLocalRandom.current();

            Color[] colors = new Color[samplesPerPixel];
            for (int i = 0; i < samplesPerPixel; i++) {
                Ray ray = camera.getRayFromOriginToCanvas(x + rnd.nextFloat() - .5f, y + rnd.nextFloat() - .5f);
                colors[i] = rayTracer.resolveRayColor(ray);
            }
            image.setRGB(x, y, Color.average(colors).toIntRGB());
        }
    }

    class SubSampler implements Sampler {

        @Override
        public void sample(RayTracer rayTracer, Camera camera, BufferedImage image, int x, int y) {
            Color[] colors = {
                    getColor(rayTracer, camera, x - .25f, y - .25f),
                    getColor(rayTracer, camera, x + .25f, y - .25f),
                    getColor(rayTracer, camera, x, y),
                    getColor(rayTracer, camera, x - .25f, y + .25f),
                    getColor(rayTracer, camera, x + .25f, y + .25f)
            };
            Color avg = Color.average(colors);
            image.setRGB(x, y, avg.toIntRGB());
        }

        private Color getColor(RayTracer rayTracer, Camera camera, float x, float y) {
            Ray ray = camera.getRayFromOriginToCanvas(x, y);
            return rayTracer.resolveRayColor(ray);
        }
    }
}
