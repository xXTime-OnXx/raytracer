package ch.hslu.raytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Sphere> spheres;
    private final List<Light> lights;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final double AMBIENT_LIGHT = 0.1; // Ambient light factor

    public Scene() {
        spheres = new ArrayList<>();
        lights = new ArrayList<>();

        // Add a default light source
        lights.add(new Light(new Vector(-5, 5, -5), Color.WHITE, 1.0));
    }

    public void addSphere(Sphere sphere) {
        spheres.add(sphere);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public Color trace(Ray ray) {
        HitInfo hitInfo = findClosestIntersection(ray);

        if (hitInfo != null) {
            return calculateColor(hitInfo);
        }
        return BACKGROUND_COLOR;
    }

    private HitInfo findClosestIntersection(Ray ray) {
        Sphere closestSphere = null;
        double closestT = Double.MAX_VALUE;

        for (Sphere sphere : spheres) {
            Double t = sphere.intersect(ray);
            if (t != null && t > 0.001 && t < closestT) { // Small epsilon to avoid self-intersection
                closestT = t;
                closestSphere = sphere;
            }
        }

        if (closestSphere != null) {
            Vector hitPoint = ray.origin().add(ray.direction().scale(closestT));
            Vector normal = hitPoint.subtract(closestSphere.getCenter()).normalize();
            return new HitInfo(closestSphere, hitPoint, normal, closestT);
        }
        return null;
    }

    private Color calculateColor(HitInfo hitInfo) {
        // Base color from the object
        Color objectColor = hitInfo.sphere.getColor();

        // Start with ambient light
        double red = objectColor.getRed() * AMBIENT_LIGHT;
        double green = objectColor.getGreen() * AMBIENT_LIGHT;
        double blue = objectColor.getBlue() * AMBIENT_LIGHT;

        // Add contribution from each light source
        for (Light light : lights) {
            // Create a vector from the hit point to the light source
            Vector lightDirection = light.position().subtract(hitInfo.hitPoint).normalize();

            // Calculate diffuse lighting using Lambert's cosine law
            double diffuseFactor = Math.max(0, hitInfo.normal.dot(lightDirection));

            // Check for shadows
            boolean inShadow = isInShadow(hitInfo.hitPoint, lightDirection);
            if (!inShadow) {
                // Add diffuse contribution
                red += objectColor.getRed() * light.intensity() * diffuseFactor * light.color().getRed() / 255.0;
                green += objectColor.getGreen() * light.intensity() * diffuseFactor * light.color().getGreen() / 255.0;
                blue += objectColor.getBlue() * light.intensity() * diffuseFactor * light.color().getBlue() / 255.0;
            }
        }

        // Clamp RGB values to valid range [0-255]
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        return new Color((int) red, (int) green, (int) blue);
    }

    private boolean isInShadow(Vector hitPoint, Vector lightDirection) {
        // Create a ray from hit point toward light
        Ray shadowRay = new Ray(hitPoint, lightDirection);

        // Check if any object blocks the light
        for (Sphere sphere : spheres) {
            Double t = sphere.intersect(shadowRay);
            if (t != null && t > 0.001) { // Small epsilon to avoid self-intersection
                return true; // This point is in shadow
            }
        }
        return false;
    }

    // Class to hold information about a ray-object intersection
    private static class HitInfo {
        final Sphere sphere;
        final Vector hitPoint;
        final Vector normal;
        final double distance;

        public HitInfo(Sphere sphere, Vector hitPoint, Vector normal, double distance) {
            this.sphere = sphere;
            this.hitPoint = hitPoint;
            this.normal = normal;
            this.distance = distance;
        }
    }
}