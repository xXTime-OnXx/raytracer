package ch.hslu.raytracer;

import java.awt.Color;

public record Light(Vector position, Color color, double intensity) {

    public Light(Vector position, Color color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = Math.max(0, Math.min(1, intensity)); // Clamp intensity between 0 and 1
    }
}
