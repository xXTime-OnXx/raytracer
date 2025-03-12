package ch.hslu.raytracer;

public record Vector(double x, double y, double z) {

    public Vector subtract(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    public Vector normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        return new Vector(x / length, y / length, z / length);
    }

    public double dot(Vector v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public Vector scale(double s) {
        return new Vector(x * s, y * s, z * s);
    }
}