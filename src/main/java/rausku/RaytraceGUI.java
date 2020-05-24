package rausku;

import rausku.algorithm.Camera;
import rausku.algorithm.RecursiveRayTracer;
import rausku.algorithm.RenderStrategy;
import rausku.algorithm.Sampler;
import rausku.math.Ray;
import rausku.scenes.Scene;
import rausku.scenes.Scene4_Transforms;

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
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class RaytraceGUI {

    private final RenderStrategy renderer;
    private final RecursiveRayTracer rayTracer;
    private final Camera camera;
    private final Sampler sampler;

    public RaytraceGUI() {

        Scene scene = new Scene4_Transforms();

        camera = scene.getCamera();
        RecursiveRayTracer.Params params = new RecursiveRayTracer.Params()
                .withMaxDepth(10);
        rayTracer = new RecursiveRayTracer(scene, params);

//        sampler = new Sampler.GaussianRandomSubSampler(8);
        sampler = new Sampler.Naive();

        renderer = new RenderStrategy.PerLineThreaded();
    }

    public static void main(String... args) {

        RaytraceGUI gui = new RaytraceGUI();

        SwingUtilities.invokeLater(gui::createGUI);
    }

    private void createGUI() {
        JLabel imageLabel = new JLabel();
        imageLabel.setVisible(false);

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
                    Image image = ((ImageIcon) imageLabel.getIcon()).getImage();
                    ImageIO.write((RenderedImage) image, "PNG", fileChooser.getSelectedFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fileMenu.add("Exit").addActionListener(actionEvent -> frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(camera.getPixelWidth(), 20));

        JPanel middlePanel = new JPanel();
        middlePanel.add(progressBar);
        middlePanel.add(imageLabel);

        JTree tree = new JTree();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Ray tree");
        TreeModel treeModel = new DefaultTreeModel(root);
        tree.setModel(treeModel);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point point = e.getPoint();
                Ray ray = camera.getRayFromOriginToCanvas(point.x, point.y);
                ray.addDebug(String.format("Canvas coordinates x=%d y=%d", point.x, point.y));
                rayTracer.resolveRayColor(1, ray);
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


        SwingWorker<BufferedImage, Void> worker = new SwingWorker<>() {

            @Override
            protected BufferedImage doInBackground() {
                return renderer.render(rayTracer, camera, sampler, this::setProgress);
            }

            @Override
            protected void done() {
                rayTracer.setDebug(true);
                try {
                    progressBar.setVisible(false);
                    imageLabel.setVisible(true);
                    imageLabel.setIcon(new ImageIcon(get()));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };

        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                Integer progress = ((Integer) evt.getNewValue());
                progressBar.setValue(progress);
            }
        });

        worker.execute();
    }

}

