package ch.hslu.raytracer;

import ch.hslu.raytracer.core.Ray;
import ch.hslu.raytracer.core.Vector;
import ch.hslu.raytracer.materials.MaterialType;
import ch.hslu.raytracer.scene.Camera;
import ch.hslu.raytracer.scene.Scene;
import ch.hslu.raytracer.scene.SceneBuilder;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RayTracer {

    public static void main(String[] args) {
        RayTracer rayTracer = new RayTracer();

        RenderSettings settings = RenderSettings.createDefault();
        Camera camera = Camera.createDefault();
        Scene scene = createDemoScene();

        long startTime = System.currentTimeMillis();
        rayTracer.renderScene(scene, camera, settings);
        long endTime = System.currentTimeMillis();

        System.out.println("Rendering completed in " + (endTime - startTime) + " ms using " +
                settings.getNumThreads() + " threads");
    }

    /**
     * Creates a demo scene with various objects and materials.
     */
    private static Scene createDemoScene() {
        return new SceneBuilder()
                // Add spheres
                .addSphere(new Vector(-1.0, 0.7, 2), 1.0, MaterialType.RUBY, 0.4)
                .addSphere(new Vector(1.0, 0.1, 1), 0.7, MaterialType.GOLD, 0.6)
                .addSphere(new Vector(0, -1001, 0), 1000, MaterialType.GREEN_PLASTIC, 0.1)

                // Add cubes
                .addRotatedCube(
                        new Vector(1.5, 0.3, 0.5), 1.0, MaterialType.PEARL, 0.3,
                        30, 45, 15
                )
                .addRotatedCube(
                        new Vector(-1.5, 0.0, 0.7), 0.8, MaterialType.SILVER, 0.7,
                        15, -30, 5
                )

                // Add lights
                .addLight(new Vector(-5, 5, -5), Color.WHITE, 1.0)
                .addLight(new Vector(3, 3, -3), new Color(200, 200, 255), 0.8)
                .build();
    }

    /**
     * Renders a scene using the specified camera and settings.
     *
     * @param scene The scene to render
     * @param camera The camera to use for rendering
     * @param settings The render settings
     */
    public void renderScene(Scene scene, Camera camera, RenderSettings settings) {
        // Set the max reflection depth in the scene
        scene.setMaxReflectionDepth(settings.getMaxReflectionDepth());

        // Create the image
        BufferedImage image = new BufferedImage(
                settings.getWidth(),
                settings.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        // Create a list of all scan lines and shuffle them randomly for better load balancing
        List<Integer> scanLines = new ArrayList<>(settings.getHeight());
        for (int y = 0; y < settings.getHeight(); y++) {
            scanLines.add(y);
        }
        Collections.shuffle(scanLines);

        // Divide the scan lines among threads
        int linesPerThread = (int) Math.ceil((double) settings.getHeight() / settings.getNumThreads());
        List<List<Integer>> threadTasks = new ArrayList<>(settings.getNumThreads());

        for (int i = 0; i < settings.getNumThreads(); i++) {
            int startIdx = i * linesPerThread;
            int endIdx = Math.min(startIdx + linesPerThread, settings.getHeight());

            if (startIdx < settings.getHeight()) {
                threadTasks.add(scanLines.subList(startIdx, endIdx));
            }
        }

        // Use CountDownLatch to wait for all threads to complete
        CountDownLatch latch = new CountDownLatch(threadTasks.size());
        ExecutorService executor = Executors.newFixedThreadPool(settings.getNumThreads());

        // Submit rendering tasks to the thread pool
        for (List<Integer> task : threadTasks) {
            executor.submit(new RenderTask(task, image, scene, camera, settings, latch));
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

        // Save the image
        try {
            ImageIO.write(image, settings.getOutputFormat(), settings.getOutputFile());
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Task for rendering a set of scan lines.
     */
    private static class RenderTask implements Runnable {
        private final List<Integer> scanLines;
        private final BufferedImage image;
        private final Scene scene;
        private final Camera camera;
        private final RenderSettings settings;
        private final CountDownLatch latch;

        public RenderTask(List<Integer> scanLines, BufferedImage image, Scene scene,
                          Camera camera, RenderSettings settings, CountDownLatch latch) {
            this.scanLines = scanLines;
            this.image = image;
            this.scene = scene;
            this.camera = camera;
            this.settings = settings;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                // Calculate aspect ratio
                double aspectRatio = (double) settings.getWidth() / settings.getHeight();

                for (int y : scanLines) {
                    for (int x = 0; x < settings.getWidth(); x++) {
                        // Convert pixel coordinates to normalized device coordinates with aspect ratio correction
                        double nx = ((x - settings.getWidth() / 2.0) / (settings.getWidth() / 2.0)) * aspectRatio;
                        double ny = -(y - settings.getHeight() / 2.0) / (settings.getHeight() / 2.0);

                        // Create a ray from the camera
                        Ray ray = camera.createRay(nx, ny);

                        // Trace the ray through the scene
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
}