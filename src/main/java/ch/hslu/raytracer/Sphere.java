package ch.hslu.raytracer;

import java.awt.*;

public class Sphere {

    private final Vector center;
    private final double radius;
    private final Color color;

    public Sphere(Vector center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public Color getColor() { return color; }

    public Double intersect(Ray ray) {
        Vector oc = ray.origin().subtract(center);
        double a = ray.direction().dot(ray.direction());
        double b = 2.0 * oc.dot(ray.direction());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;
        return (-b - Math.sqrt(discriminant)) / (2.0 * a);
    }

}