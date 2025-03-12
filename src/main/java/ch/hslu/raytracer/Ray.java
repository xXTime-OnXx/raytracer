package ch.hslu.raytracer;

public record Ray(Vector origin, Vector direction) {

    public Ray(Vector origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

}