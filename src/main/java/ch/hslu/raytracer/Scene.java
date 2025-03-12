package ch.hslu.raytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Sphere> spheres;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final double MAX_DEPTH = 40.0; // Maximum depth for fading

    public Scene() {
        spheres = new ArrayList<>();
    }

    public void addSphere(Sphere sphere) {
        spheres.add(sphere);
    }

    public Color trace(Ray ray) {
        Sphere closestSphere = null;
        double closestT = Double.MAX_VALUE;

        for (Sphere sphere : spheres) {
            Double t = sphere.intersect(ray);
            if (t != null && t < closestT) {
                closestT = t;
                closestSphere = sphere;
            }
        }

        if (closestSphere != null) {
            double depthFactor = Math.max(0, 1 - (closestT / MAX_DEPTH)); // Normalize depth
            return blendColors(closestSphere.getColor(), BACKGROUND_COLOR, depthFactor);
        }
        return BACKGROUND_COLOR;
    }

    private Color blendColors(Color foreground, Color background, double factor) {
        int r = (int) ((foreground.getRed() * factor) + (background.getRed() * (1 - factor)));
        int g = (int) ((foreground.getGreen() * factor) + (background.getGreen() * (1 - factor)));
        int b = (int) ((foreground.getBlue() * factor) + (background.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }
}