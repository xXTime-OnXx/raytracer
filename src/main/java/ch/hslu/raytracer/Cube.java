package ch.hslu.raytracer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Cube extends Object3D {
    private final Vector center;
    private final double size;
    private final List<Triangle> triangles;

    public Cube(Vector center, double size, Color color) {
        super(color);
        this.center = center;
        this.size = size;
        this.triangles = createTriangles();
    }

    private List<Triangle> createTriangles() {
        List<Triangle> result = new ArrayList<>(12); // A cube has 12 triangles (2 per face)

        // Calculate half-size for vertex positions
        double hs = size / 2.0;

        // Define the 8 vertices of the cube
        Vector v0 = new Vector(center.x() - hs, center.y() - hs, center.z() - hs); // bottom-left-back
        Vector v1 = new Vector(center.x() + hs, center.y() - hs, center.z() - hs); // bottom-right-back
        Vector v2 = new Vector(center.x() + hs, center.y() + hs, center.z() - hs); // top-right-back
        Vector v3 = new Vector(center.x() - hs, center.y() + hs, center.z() - hs); // top-left-back
        Vector v4 = new Vector(center.x() - hs, center.y() - hs, center.z() + hs); // bottom-left-front
        Vector v5 = new Vector(center.x() + hs, center.y() - hs, center.z() + hs); // bottom-right-front
        Vector v6 = new Vector(center.x() + hs, center.y() + hs, center.z() + hs); // top-right-front
        Vector v7 = new Vector(center.x() - hs, center.y() + hs, center.z() + hs); // top-left-front

        // Front face
        result.add(new Triangle(v4, v5, v6, color));
        result.add(new Triangle(v4, v6, v7, color));

        // Back face
        result.add(new Triangle(v1, v0, v3, color));
        result.add(new Triangle(v1, v3, v2, color));

        // Left face
        result.add(new Triangle(v0, v4, v7, color));
        result.add(new Triangle(v0, v7, v3, color));

        // Right face
        result.add(new Triangle(v5, v1, v2, color));
        result.add(new Triangle(v5, v2, v6, color));

        // Top face
        result.add(new Triangle(v7, v6, v2, color));
        result.add(new Triangle(v7, v2, v3, color));

        // Bottom face
        result.add(new Triangle(v0, v1, v5, color));
        result.add(new Triangle(v0, v5, v4, color));

        return result;
    }

    @Override
    public HitInfo intersect(Ray ray) {
        HitInfo closestHit = null;
        double closestDistance = Double.MAX_VALUE;

        // Check intersection with all triangles
        for (Triangle triangle : triangles) {
            HitInfo hit = triangle.intersect(ray);
            if (hit != null && hit.getDistance() < closestDistance) {
                closestHit = new HitInfo(this, hit.getHitPoint(), hit.getNormal(), hit.getDistance());
                closestDistance = hit.getDistance();
            }
        }

        return closestHit;
    }
}
