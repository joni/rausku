package com.jsalonen.raytrace;

import com.jsalonen.raytrace.scenes.Scene7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Raytrace {

    public static void main(String... args) {

        Scene scene = new Scene7();

        Camera camera = scene.getCamera();

        int pixelWidth = camera.getPixelWidth();
        int pixelHeight = camera.getPixelHeight();

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage debugimage = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixelHeight; y++) {
            for (int x = 0; x < pixelWidth; x++) {
                Ray ray = camera.getRayFromOriginToCanvas(x, y);

                Color color = scene.resolveRayColor(1, ray);

                image.setRGB(x, y, color.toIntRGB());
                Color debugcolor = scene.resolveRayColorDebug(1, ray, false);
                debugimage.setRGB(x, y, debugcolor.toIntRGB());
            }
        }

        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JLabel label = new JLabel(new ImageIcon(image));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Point point = e.getPoint();
                            Ray ray = camera.getRayFromOriginToCanvas(point.x, point.y);

                            scene.resolveRayColorDebug(1, ray, true);

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

