package com.jsalonen.raytrace;

import com.jsalonen.raytrace.geometry.Vec;
import com.jsalonen.raytrace.scenes.Scene6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Raytrace {
    public static void main(String... args) {
        int pixelWidth = 768;
        int pixelHeight = 768;
        Canvas canvas = new Canvas(pixelWidth, pixelHeight, 1280, Vec.of(0, 0, -10), 1);

        Scene scene = new Scene6();

        BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        BufferedImage debugimage = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < pixelHeight; y++) {
            for (int x = 0; x < pixelWidth; x++) {
                Ray ray = canvas.getRayFromOriginToCanvas(x, y);

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
                            Ray ray = canvas.getRayFromOriginToCanvas(point.x, point.y);

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

