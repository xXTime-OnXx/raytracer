package ch.hslu.raytracer.scene;

import ch.hslu.raytracer.core.Ray;
import ch.hslu.raytracer.core.Vector;

/**
 * Represents a camera in the scene.
 */
public class Camera {
    private final Vector position;

    /**
     * Creates a camera with the specified parameters.
     *
     * @param position The position of the camera
     */
    public Camera(Vector position) {
        this.position = position;
    }

    /**
     * Creates a default camera positioned at (0, 0, -5) looking toward the origin.
     */
    public static Camera createDefault() {
        return new Camera(
                new Vector(0, 0, -5)
        );
    }

    /**
     * Creates a ray from the camera for the given normalized screen coordinates.
     *
     * @param nx Normalized x-coordinate (between -1 and 1)
     * @param ny Normalized y-coordinate (between -1 and 1)
     * @return The ray from the camera through the specified screen point
     */
    public Ray createRay(double nx, double ny) {
        // This simpler implementation matches the original ray generation logic
        // The nx/ny values are already normalized in the RayTracer
        Vector direction = new Vector(nx, ny, 1).normalize();
        return new Ray(position, direction);
    }

}