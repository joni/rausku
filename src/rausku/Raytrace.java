package rausku;

import rausku.scenes.Scene10;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Raytrace {

    public static void main(String... args) {

        Scene scene = new Scene10();

        Camera camera = scene.getCamera();

        Sampler sampler = new Sampler.GaussianRandomSubSampler(4);

        RenderStrategy renderer = new RenderStrategy.TimedStrategyDecorator(new RenderStrategy.PerLineThreaded());
//        RenderStrategy renderer = new RenderStrategy.TimedStrategyDecorator(new RenderStrategy.SingleThreaded());

        BufferedImage image = renderer.render(scene, camera, sampler);

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
                    JPanel middlePanel = new JPanel();
                    middlePanel.add(label);

                    JTree tree = new JTree();
                    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Ray tree");
                    TreeModel treeModel = new DefaultTreeModel(root);
                    tree.setModel(treeModel);

                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Point point = e.getPoint();
                            Ray ray = camera.getRayFromOriginToCanvas(point.x, point.y);
                            ray.addDebug(String.format("Canvas coordinates x=%d y=%d", point.x, point.y));
                            ray.addDebug(String.format("%s", label));
                            scene.resolveRayColor(1, ray);
                            DefaultMutableTreeNode root = buildDebugTree(ray);
                            tree.setModel(new DefaultTreeModel(root));
                            tree.revalidate();
                        }

                        private DefaultMutableTreeNode buildDebugTree(Debuggable debuggable) {
                            DefaultMutableTreeNode root = new DefaultMutableTreeNode(debuggable);
                            for (Object debugInfo : debuggable.getDebugInfo()) {
                                if (debugInfo instanceof Debuggable) {
                                    DefaultMutableTreeNode childTree = buildDebugTree((Debuggable) debugInfo);
                                    root.add(childTree);
                                } else {
                                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(debugInfo);
                                    root.add(node);
                                }
                            }
                            return root;
                        }
                    });

                    JScrollPane scrollPane = new JScrollPane(tree);
                    scrollPane.setPreferredSize(new Dimension(500, 500));

                    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, middlePanel, scrollPane);

                    frame.add(splitPane);
                    frame.pack();
                    frame.setVisible(true);
                }
        );
    }
}

