package ch.hslu.raytracer;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RayTracer {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final Vector CAMERA = new Vector(0, 0, -5);
    private static final int NUM_THREADS = Runtime.getRuntime().availableProcessors(); // Use available CPU cores

    public static void main(String[] args) {
        RayTracer rayTracer = new RayTracer();
        long startTime = System.currentTimeMillis();
        rayTracer.renderScene();
        long endTime = System.currentTimeMillis();
        System.out.println("Rendering completed in " + (endTime - startTime) + " ms using " + NUM_THREADS + " threads");
    }

    public void renderScene() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Scene scene = new Scene();

        // Add spheres
        scene.addSphere(new Sphere(new Vector(-1.0, 0.7, 2), 1, new Color(0, 180, 180))); // Cyan sphere
        scene.addSphere(new Sphere(new Vector(1.0, 0.1, 1), 0.7, new Color(220, 100, 100))); // Red sphere
        scene.addSphere(new Sphere(new Vector(0, -1001, 0), 1000, new Color(200, 200, 100))); // Ground plane as huge sphere

        // Add lights
        scene.addLight(new Light(new Vector(-5, 5, -5), Color.WHITE, 0.8)); // Main light
        scene.addLight(new Light(new Vector(3, 3, -3), new Color(200, 200, 255), 0.4)); // Fill light

        // Create a list of all scan lines and shuffle them randomly
        List<Integer> scanLines = new ArrayList<>(HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            scanLines.add(y);
        }
        Collections.shuffle(scanLines);

        // Divide the scan lines among threads
        int linesPerThread = (int) Math.ceil((double) HEIGHT / NUM_THREADS);
        List<List<Integer>> threadTasks = new ArrayList<>(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            int startIdx = i * linesPerThread;
            int endIdx = Math.min(startIdx + linesPerThread, HEIGHT);

            if (startIdx < HEIGHT) {
                threadTasks.add(scanLines.subList(startIdx, endIdx));
            }
        }

        // Use CountDownLatch to wait for all threads to complete
        CountDownLatch latch = new CountDownLatch(threadTasks.size());
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        // Submit rendering tasks to the thread pool
        for (List<Integer> task : threadTasks) {
            executor.submit(new RenderTask(task, image, scene, latch));
        }

        // Wait for all threads to finish
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Rendering interrupted: " + e.getMessage());
        }

        // Shut down the executor
        executor.shutdown();

        saveImage(image);
    }

    private static class RenderTask implements Runnable {
        private final List<Integer> scanLines;
        private final BufferedImage image;
        private final Scene scene;
        private final CountDownLatch latch;

        public RenderTask(List<Integer> scanLines, BufferedImage image, Scene scene, CountDownLatch latch) {
            this.scanLines = scanLines;
            this.image = image;
            this.scene = scene;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                for (int y : scanLines) {
                    for (int x = 0; x < WIDTH; x++) {
                        double nx = (x - WIDTH / 2.0) / WIDTH;
                        double ny = -(y - HEIGHT / 2.0) / HEIGHT;
                        Ray ray = new Ray(CAMERA, new Vector(nx, ny, 1));
                        Color pixelColor = scene.trace(ray);

                        // Synchronize access to the shared image
                        synchronized (image) {
                            image.setRGB(x, y, pixelColor.getRGB());
                        }
                    }
                }
            } finally {
                latch.countDown();
            }
        }
    }

    private void saveImage(BufferedImage image) {
        try {
            ImageIO.write(image, "png", new File("raytraced_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}