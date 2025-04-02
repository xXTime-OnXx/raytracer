package ch.hslu.raytracer.core;

import ch.hslu.raytracer.objects.Object3D;

public class HitInfo {
    private final Object3D object;
    private final Vector hitPoint;
    private final Vector normal;
    private final double distance;

    public HitInfo(Object3D object, Vector hitPoint, Vector normal, double distance) {
        this.object = object;
        this.hitPoint = hitPoint;
        this.normal = normal;
        this.distance = distance;
    }

    public Object3D getObject() {
        return object;
    }

    public Vector getHitPoint() {
        return hitPoint;
    }

    public Vector getNormal() {
        return normal;
    }

    public double getDistance() {
        return distance;
    }
}