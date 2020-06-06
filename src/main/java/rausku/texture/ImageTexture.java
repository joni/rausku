package rausku.texture;

import rausku.geometry.Intercept;
import rausku.lighting.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ImageTexture implements Texture {

    private final float minU;
    private final float maxU;
    private final float minV;
    private final float maxV;
    private BufferedImage image;

    public ImageTexture(String fileName, float minU, float maxU, float minV, float maxV) throws IOException {
        this(ImageIO.read(new File(fileName)), minU, maxU, minV, maxV);
    }

    public ImageTexture(URL imageUrl, float minU, float maxU, float minV, float maxV) throws IOException {
        this(ImageIO.read(imageUrl), minU, maxU, minV, maxV);
    }

    public ImageTexture(BufferedImage image, float minU, float maxU, float minV, float maxV) {
        this.image = image;
        this.minU = minU;
        this.maxU = maxU;
        this.minV = minV;
        this.maxV = maxV;
    }

    @Override
    public Color getColor(Intercept intercept) {
        int x = (int) (remapU(intercept.u) * image.getWidth());
        int y = (int) (remapV(intercept.v) * image.getHeight());
        int rgb = image.getRGB(x, y);
        return Color.ofRgb(rgb);
    }

    float remapU(float value) {
        return (value - minU) / (maxU - minU);
    }

    float remapV(float value) {
        return (value - minV) / (maxV - minV);
    }
}
