package rausku;

import rausku.scenes.Scene9;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Raytrace {

    public static void main(String... args) {

        Scene scene = new Scene9();

        Camera camera = scene.getCamera();

        Sampler sampler = new Sampler.RandomSubsampler(64);

        BufferedImage image = sampler.sample(scene, camera);

        scene.debug = true;

        SwingUtilities.invokeLater(() -> {
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JMenuBar menubar = new JMenuBar();
                    frame.setJMenuBar(menubar);
                    JMenu fileMenu = new JMenu("File");
                    menubar.add(fileMenu);
                    fileMenu.add("Save").addActionListener(actionEvent -> {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                        int save = fileChooser.showDialog(frame, "Save");
                        if (save == JFileChooser.APPROVE_OPTION) {
                            try {
                                ImageIO.write(image, "PNG", fileChooser.getSelectedFile());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    fileMenu.add("Exit").addActionListener(actionEvent -> {
                        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                    });

                    JLabel label = new JLabel(new ImageIcon(image));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Point point = e.getPoint();
                            Ray ray = camera.getRayFromOriginToCanvas(point.x, point.y);
                            scene.resolveRayColor(1, ray);
                        }
                    });
                    frame.add(label);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

