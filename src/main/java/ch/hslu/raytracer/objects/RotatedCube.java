package ch.hslu.raytracer.objects;

import ch.hslu.raytracer.core.HitInfo;
import ch.hslu.raytracer.core.Ray;
import ch.hslu.raytracer.core.Vector;
import ch.hslu.raytracer.materials.Material;

import java.util.ArrayList;
import java.util.List;

public class RotatedCube extends Object3D {
    private final Vector center;
    private final double size;
    private final double rotationX; // Rotation around X-axis in radians
    private final double rotationY; // Rotation around Y-axis in radians
    private final double rotationZ; // Rotation around Z-axis in radians
    private final List<Triangle> triangles;

    public RotatedCube(Vector center, double size, Material material, double rotationX, double rotationY, double rotationZ) {
        super(material);
        this.center = center;
        this.size = size;
        this.rotationX = rotationX;
        this.rotationY = rotationY;
        this.rotationZ = rotationZ;
        this.triangles = createTriangles();
    }

    private List<Triangle> createTriangles() {
        List<Triangle> result = new ArrayList<>(12); // A cube has 12 triangles (2 per face)

        // Calculate half-size for vertex positions
        double hs = size / 2.0;

        // Define the 8 vertices of the cube (before rotation)
        Vector[] vertices = new Vector[8];
        vertices[0] = new Vector(-hs, -hs, -hs); // bottom-left-back
        vertices[1] = new Vector(hs, -hs, -hs);  // bottom-right-back
        vertices[2] = new Vector(hs, hs, -hs);   // top-right-back
        vertices[3] = new Vector(-hs, hs, -hs);  // top-left-back
        vertices[4] = new Vector(-hs, -hs, hs);  // bottom-left-front
        vertices[5] = new Vector(hs, -hs, hs);   // bottom-right-front
        vertices[6] = new Vector(hs, hs, hs);    // top-right-front
        vertices[7] = new Vector(-hs, hs, hs);   // top-left-front

        // Apply rotations and translation to all vertices
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = rotateVertex(vertices[i]);
            vertices[i] = new Vector(
                    vertices[i].x() + center.x(),
                    vertices[i].y() + center.y(),
                    vertices[i].z() + center.z()
            );
        }

        // Front face
        result.add(new Triangle(vertices[4], vertices[5], vertices[6], material));
        result.add(new Triangle(vertices[4], vertices[6], vertices[7], material));

        // Back face
        result.add(new Triangle(vertices[1], vertices[0], vertices[3], material));
        result.add(new Triangle(vertices[1], vertices[3], vertices[2], material));

        // Left face
        result.add(new Triangle(vertices[0], vertices[4], vertices[7], material));
        result.add(new Triangle(vertices[0], vertices[7], vertices[3], material));

        // Right face
        result.add(new Triangle(vertices[5], vertices[1], vertices[2], material));
        result.add(new Triangle(vertices[5], vertices[2], vertices[6], material));

        // Top face
        result.add(new Triangle(vertices[7], vertices[6], vertices[2], material));
        result.add(new Triangle(vertices[7], vertices[2], vertices[3], material));

        // Bottom face
        result.add(new Triangle(vertices[0], vertices[1], vertices[5], material));
        result.add(new Triangle(vertices[0], vertices[5], vertices[4], material));

        return result;
    }

    private Vector rotateVertex(Vector v) {
        // Apply X rotation
        double y1 = v.y() * Math.cos(rotationX) - v.z() * Math.sin(rotationX);
        double z1 = v.y() * Math.sin(rotationX) + v.z() * Math.cos(rotationX);

        // Apply Y rotation
        double x2 = v.x() * Math.cos(rotationY) + z1 * Math.sin(rotationY);
        double z2 = -v.x() * Math.sin(rotationY) + z1 * Math.cos(rotationY);

        // Apply Z rotation
        double x3 = x2 * Math.cos(rotationZ) - y1 * Math.sin(rotationZ);
        double y3 = x2 * Math.sin(rotationZ) + y1 * Math.cos(rotationZ);

        return new Vector(x3, y3, z2);
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