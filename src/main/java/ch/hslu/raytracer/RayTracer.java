package ch.hslu.raytracer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RayTracer {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final Vector CAMERA = new Vector(0, 0, -5);

    public static void main(String[] args) {
        RayTracer rayTracer = new RayTracer();
        rayTracer.renderScene();
    }

    public void renderScene() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Scene scene = new Scene();

        scene.addSphere(new Sphere(new Vector(-0.7, 0.7, 1), 1, Color.CYAN));
        scene.addSphere(new Sphere(new Vector(0, 0, 2), 2, Color.BLUE));

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                double nx = (x - WIDTH / 2.0) / WIDTH;
                double ny = -(y - HEIGHT / 2.0) / HEIGHT;
                Ray ray = new Ray(CAMERA, new Vector(nx, ny, 1));
                Color pixelColor = scene.trace(ray);
                image.setRGB(x, y, pixelColor.getRGB());
            }
        }

        saveImage(image);
    }

    private void saveImage(BufferedImage image) {
        try {
            ImageIO.write(image, "png", new File("raytraced_image.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}