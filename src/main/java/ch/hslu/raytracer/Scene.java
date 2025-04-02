package ch.hslu.raytracer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scene {

    private final List<Object3D> objects;
    private final List<Light> lights;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final int MAX_REFLECTION_DEPTH = 10; // Maximum reflection depth to prevent infinite recursion

    public Scene() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public void addSphere(Sphere sphere) {
        objects.add(sphere);
    }

    public void addRotatedCube(RotatedCube cube) {
        objects.add(cube);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public Color trace(Ray ray) {
        return trace(ray, 0);
    }

    private Color trace(Ray ray, int depth) {
        HitInfo hitInfo = findClosestIntersection(ray);

        if (hitInfo != null) {
            return calculateColor(hitInfo, ray, depth);
        }
        return BACKGROUND_COLOR;
    }

    private HitInfo findClosestIntersection(Ray ray) {
        HitInfo closestHit = null;
        double closestDistance = Double.MAX_VALUE;

        for (Object3D object : objects) {
            HitInfo hit = object.intersect(ray);
            if (hit != null && hit.getDistance() > 0.001 && hit.getDistance() < closestDistance) {
                closestHit = hit;
                closestDistance = hit.getDistance();
            }
        }

        return closestHit;
    }

    private Color calculateColor(HitInfo hitInfo, Ray ray, int depth) {
        Material material = hitInfo.getObject().getMaterial();
        Vector hitPoint = hitInfo.getHitPoint();
        Vector normal = hitInfo.getNormal();
        Vector viewDirection = ray.direction().scale(-1).normalize();

        // Start with ambient light component
        Color ambientColor = material.getAmbient();
        double red = ambientColor.getRed();
        double green = ambientColor.getGreen();
        double blue = ambientColor.getBlue();

        // Add contribution from each light source (diffuse and specular)
        for (Light light : lights) {
            // Create a vector from the hit point to the light source
            Vector lightDirection = light.position().subtract(hitPoint).normalize();

            // Check for shadows
            boolean inShadow = isInShadow(hitPoint, lightDirection);

            if (!inShadow) {
                // Calculate diffuse lighting using Lambert's cosine law
                double diffuseFactor = Math.max(0, normal.dot(lightDirection));

                // Diffuse component
                Color diffuseColor = material.getDiffuse();
                red += diffuseColor.getRed() * light.intensity() * diffuseFactor * light.color().getRed() / 255.0;
                green += diffuseColor.getGreen() * light.intensity() * diffuseFactor * light.color().getGreen() / 255.0;
                blue += diffuseColor.getBlue() * light.intensity() * diffuseFactor * light.color().getBlue() / 255.0;

                // Calculate specular lighting (Phong model)
                Vector reflectionDir = reflect(lightDirection.scale(-1), normal);
                double specularFactor = Math.pow(Math.max(0, reflectionDir.dot(viewDirection)),
                        material.getShininess() * 128); // Scale shininess to make it more noticeable

                // Specular component
                Color specularColor = material.getSpecular();
                red += specularColor.getRed() * light.intensity() * specularFactor * light.color().getRed() / 255.0;
                green += specularColor.getGreen() * light.intensity() * specularFactor * light.color().getGreen() / 255.0;
                blue += specularColor.getBlue() * light.intensity() * specularFactor * light.color().getBlue() / 255.0;
            }
        }

        // Add reflection component if we haven't reached the maximum depth
        double reflectivity = material.getReflectivity();
        if (reflectivity > 0 && depth < MAX_REFLECTION_DEPTH) {
            Vector reflectionDir = reflect(ray.direction(), normal);
            Ray reflectionRay = new Ray(hitPoint, reflectionDir);

            // Get the color from the reflection ray
            Color reflectionColor = trace(reflectionRay, depth + 1);

            // Add reflection component weighted by reflectivity
            red = red * (1 - reflectivity) + reflectionColor.getRed() * reflectivity;
            green = green * (1 - reflectivity) + reflectionColor.getGreen() * reflectivity;
            blue = blue * (1 - reflectivity) + reflectionColor.getBlue() * reflectivity;
        }

        // Clamp RGB values to valid range [0-255]
        red = Math.min(255, Math.max(0, red));
        green = Math.min(255, Math.max(0, green));
        blue = Math.min(255, Math.max(0, blue));

        return new Color((int) red, (int) green, (int) blue);
    }

    // Helper method to calculate reflection vector
    private Vector reflect(Vector incident, Vector normal) {
        double dot = incident.dot(normal);
        return incident.subtract(normal.scale(2 * dot));
    }

    private boolean isInShadow(Vector hitPoint, Vector lightDirection) {
        // Create a ray from hit point toward light
        Ray shadowRay = new Ray(hitPoint, lightDirection);

        // Check if any object blocks the light
        for (Object3D object : objects) {
            HitInfo hit = object.intersect(shadowRay);
            if (hit != null && hit.getDistance() > 0.001) { // Small epsilon to avoid self-intersection
                return true; // This point is in shadow
            }
        }
        return false;
    }
}