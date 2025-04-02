package ch.hslu.raytracer.objects;

import ch.hslu.raytracer.core.HitInfo;
import ch.hslu.raytracer.core.Ray;
import ch.hslu.raytracer.materials.Material;

public abstract class Object3D {
    protected final Material material;

    public Object3D(Material material) {
        this.material = material;
    }

    /**
     * Tests if a ray intersects with this object.
     *
     * @param ray The ray to test for intersection
     * @return Information about the hit, or null if no intersection
     */
    public abstract HitInfo intersect(Ray ray);

    /**
     * Gets the material of this object.
     *
     * @return The material of the object
     */
    public Material getMaterial() {
        return material;
    }

}