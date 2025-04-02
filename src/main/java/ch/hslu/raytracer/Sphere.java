package ch.hslu.raytracer;

public class Sphere extends Object3D {

    private final Vector center;
    private final double radius;

    public Sphere(Vector center, double radius, Material material) {
        super(material);
        this.center = center;
        this.radius = radius;
    }

    @Override
    public HitInfo intersect(Ray ray) {
        Vector oc = ray.origin().subtract(center);
        double a = ray.direction().dot(ray.direction());
        double b = 2.0 * oc.dot(ray.direction());
        double c = oc.dot(oc) - radius * radius;
        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) return null;

        double t1 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
        double t2 = (-b + Math.sqrt(discriminant)) / (2.0 * a);

        // Get the closest positive intersection
        double t;
        if (t1 > 0.0001) {
            t = t1;
        } else if (t2 > 0.0001) {
            t = t2;
        } else {
            return null; // No valid intersection
        }

        // Calculate intersection point and normal
        Vector hitPoint = ray.origin().add(ray.direction().scale(t));
        Vector normal = hitPoint.subtract(center).normalize();

        return new HitInfo(this, hitPoint, normal, t);
    }
}