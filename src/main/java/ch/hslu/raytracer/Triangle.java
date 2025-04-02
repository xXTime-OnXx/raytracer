package ch.hslu.raytracer;

public class Triangle extends Object3D {
    private final Vector v0, v1, v2; // Vertices
    private final Vector normal;     // Normal vector

    public Triangle(Vector v0, Vector v1, Vector v2, Material material) {
        super(material);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;

        // Calculate normal using cross product of two edges
        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);
        this.normal = edge1.cross(edge2).normalize();
    }

    @Override
    public HitInfo intersect(Ray ray) {
        // Möller–Trumbore intersection algorithm
        Vector edge1 = v1.subtract(v0);
        Vector edge2 = v2.subtract(v0);

        Vector h = ray.direction().cross(edge2);
        double a = edge1.dot(h);

        // If a is too close to 0, ray is parallel to the triangle
        if (Math.abs(a) < 0.0001) {
            return null;
        }

        double f = 1.0 / a;
        Vector s = ray.origin().subtract(v0);
        double u = f * s.dot(h);

        // Check if intersection point is outside the triangle
        if (u < 0.0 || u > 1.0) {
            return null;
        }

        Vector q = s.cross(edge1);
        double v = f * ray.direction().dot(q);

        // Check if intersection point is outside the triangle
        if (v < 0.0 || u + v > 1.0) {
            return null;
        }

        // Calculate t (distance along the ray)
        double t = f * edge2.dot(q);

        // Check if intersection is behind the ray origin
        if (t <= 0.0001) {
            return null;
        }

        // Calculate the intersection point
        Vector hitPoint = ray.origin().add(ray.direction().scale(t));

        // Ensure normal faces the right way (opposite to the incoming ray)
        Vector finalNormal = normal;
        if (normal.dot(ray.direction()) > 0) {
            finalNormal = normal.scale(-1);  // Flip normal if needed
        }

        return new HitInfo(this, hitPoint, finalNormal, t);
    }
}