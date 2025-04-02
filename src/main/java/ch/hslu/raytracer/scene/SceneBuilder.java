package ch.hslu.raytracer.scene;

import ch.hslu.raytracer.core.Vector;
import ch.hslu.raytracer.materials.Material;
import ch.hslu.raytracer.materials.MaterialType;
import ch.hslu.raytracer.objects.RotatedCube;
import ch.hslu.raytracer.objects.Sphere;

import java.awt.Color;

/**
 * Builder class for creating scenes with a fluent API.
 */
public class SceneBuilder {
    private final Scene scene;

    public SceneBuilder() {
        scene = new Scene();
    }

    /**
     * Adds a sphere to the scene.
     */
    public SceneBuilder addSphere(Vector center, double radius, MaterialType material, double reflectivity) {
        scene.addSphere(new Sphere(center, radius, Material.create(material, reflectivity)));
        return this;
    }

    /**
     * Adds a rotated cube to the scene.
     */
    public SceneBuilder addRotatedCube(Vector center, double size, MaterialType material, double reflectivity,
                                       double rotationX, double rotationY, double rotationZ) {
        scene.addRotatedCube(new RotatedCube(
                center, size, Material.create(material, reflectivity),
                Math.toRadians(rotationX), Math.toRadians(rotationY), Math.toRadians(rotationZ)
        ));
        return this;
    }

    /**
     * Adds a light source to the scene.
     */
    public SceneBuilder addLight(Vector position, Color color, double intensity) {
        scene.addLight(new Light(position, color, intensity));
        return this;
    }

    /**
     * Builds and returns the constructed scene.
     */
    public Scene build() {
        return scene;
    }
}