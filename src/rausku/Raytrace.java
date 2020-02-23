package rausku;

import rausku.scenes.Scene8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Raytrace {

    public static void main(String... args) {

        Scene scene = new Scene8();

        Camera camera = scene.getCamera();

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

        scene.debug = true;

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JLabel label = new JLabel(new ImageIcon(image));
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Point point = e.getPoint();
                    Ray ray = camera.getRayFromOriginToCanvas(point.x, point.y);

                    scene.resolveRayColor(0, ray);

                            //System.exit(0);
                        }
                    });
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

