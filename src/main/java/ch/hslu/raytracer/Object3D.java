package ch.hslu.raytracer;

import java.awt.Color;

public abstract class Object3D {
    protected final Color color;

    public Object3D(Color color) {
        this.color = color;
    }

    /**
     * Tests if a ray intersects with this object.
     *
     * @param ray The ray to test for intersection
     * @return Information about the hit, or null if no intersection
     */
    public abstract HitInfo intersect(Ray ray);

    /**
     * Gets the color of this object.
     *
     * @return The color of the object
     */
    public Color getColor() {
        return color;
    }
}
