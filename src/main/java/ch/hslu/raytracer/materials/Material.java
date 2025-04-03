package ch.hslu.raytracer.materials;

import lombok.Getter;

import java.awt.Color;

@Getter
public class Material {
    private final MaterialType type;
    private final Color ambient;
    private final Color diffuse;
    private final Color specular;
    private final double shininess;
    private final double reflectivity;

    /**
     * Creates a material with the specified properties.
     *
     * @param type        The type of material
     * @param ambient     The ambient color component
     * @param diffuse     The diffuse color component
     * @param specular    The specular color component
     * @param shininess   The shininess factor (0-1)
     * @param reflectivity The reflectivity factor (0-1)
     */
    public Material(MaterialType type, Color ambient, Color diffuse, Color specular, double shininess, double reflectivity) {
        this.type = type;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
        this.reflectivity = Math.max(0, Math.min(1, reflectivity)); // Clamp reflectivity between 0 and 1
    }

    /**
     * Creates a material from a predefined type.
     *
     * @param type        The material type
     * @param reflectivity The reflectivity factor (0-1)
     * @return A new Material instance
     */
    public static Material create(MaterialType type, double reflectivity) {
        return switch (type) {
            case EMERALD -> new Material(type,
                    new Color(0.0215f, 0.1745f, 0.0215f),
                    new Color(0.07568f, 0.61424f, 0.07568f),
                    new Color(0.633f, 0.727811f, 0.633f),
                    0.6, reflectivity);
            case JADE -> new Material(type,
                    new Color(0.135f, 0.2225f, 0.1575f),
                    new Color(0.54f, 0.89f, 0.63f),
                    new Color(0.316228f, 0.316228f, 0.316228f),
                    0.1, reflectivity);
            case OBSIDIAN -> new Material(type,
                    new Color(0.05375f, 0.05f, 0.06625f),
                    new Color(0.18275f, 0.17f, 0.22525f),
                    new Color(0.332741f, 0.328634f, 0.346435f),
                    0.3, reflectivity);
            case PEARL -> new Material(type,
                    new Color(0.25f, 0.20725f, 0.20725f),
                    new Color(1.0f, 0.829f, 0.829f),
                    new Color(0.296648f, 0.296648f, 0.296648f),
                    0.088, reflectivity);
            case RUBY -> new Material(type,
                    new Color(0.1745f, 0.01175f, 0.01175f),
                    new Color(0.61424f, 0.04136f, 0.04136f),
                    new Color(0.727811f, 0.626959f, 0.626959f),
                    0.6, reflectivity);
            case TURQUOISE -> new Material(type,
                    new Color(0.1f, 0.18725f, 0.1745f),
                    new Color(0.396f, 0.74151f, 0.69102f),
                    new Color(0.297254f, 0.30829f, 0.306678f),
                    0.1, reflectivity);
            case BRASS -> new Material(type,
                    new Color(0.329412f, 0.223529f, 0.027451f),
                    new Color(0.780392f, 0.568627f, 0.113725f),
                    new Color(0.992157f, 0.941176f, 0.807843f),
                    0.21794872, reflectivity);
            case BRONZE -> new Material(type,
                    new Color(0.2125f, 0.1275f, 0.054f),
                    new Color(0.714f, 0.4284f, 0.18144f),
                    new Color(0.393548f, 0.271906f, 0.166721f),
                    0.2, reflectivity);
            case CHROME -> new Material(type,
                    new Color(0.25f, 0.25f, 0.25f),
                    new Color(0.4f, 0.4f, 0.4f),
                    new Color(0.774597f, 0.774597f, 0.774597f),
                    0.6, reflectivity);
            case COPPER -> new Material(type,
                    new Color(0.19125f, 0.0735f, 0.0225f),
                    new Color(0.7038f, 0.27048f, 0.0828f),
                    new Color(0.256777f, 0.137622f, 0.086014f),
                    0.1, reflectivity);
            case GOLD -> new Material(type,
                    new Color(0.24725f, 0.1995f, 0.0745f),
                    new Color(0.75164f, 0.60648f, 0.22648f),
                    new Color(0.628281f, 0.555802f, 0.366065f),
                    0.4, reflectivity);
            case SILVER -> new Material(type,
                    new Color(0.19225f, 0.19225f, 0.19225f),
                    new Color(0.50754f, 0.50754f, 0.50754f),
                    new Color(0.508273f, 0.508273f, 0.508273f),
                    0.4, reflectivity);
            case BLACK_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.0f, 0.0f),
                    new Color(0.01f, 0.01f, 0.01f),
                    new Color(0.50f, 0.50f, 0.50f),
                    0.25, reflectivity);
            case CYAN_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.1f, 0.06f),
                    new Color(0.0f, 0.50980392f, 0.50980392f),
                    new Color(0.50196078f, 0.50196078f, 0.50196078f),
                    0.25, reflectivity);
            case GREEN_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.0f, 0.0f),
                    new Color(0.1f, 0.35f, 0.1f),
                    new Color(0.45f, 0.55f, 0.45f),
                    0.25, reflectivity);
            case RED_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.0f, 0.0f),
                    new Color(0.5f, 0.0f, 0.0f),
                    new Color(0.7f, 0.6f, 0.6f),
                    0.25, reflectivity);
            case WHITE_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.0f, 0.0f),
                    new Color(0.55f, 0.55f, 0.55f),
                    new Color(0.70f, 0.70f, 0.70f),
                    0.25, reflectivity);
            case YELLOW_PLASTIC -> new Material(type,
                    new Color(0.0f, 0.0f, 0.0f),
                    new Color(0.5f, 0.5f, 0.0f),
                    new Color(0.60f, 0.60f, 0.50f),
                    0.25, reflectivity);
            case BLACK_RUBBER -> new Material(type,
                    new Color(0.02f, 0.02f, 0.02f),
                    new Color(0.01f, 0.01f, 0.01f),
                    new Color(0.4f, 0.4f, 0.4f),
                    0.078125, reflectivity);
            case CYAN_RUBBER -> new Material(type,
                    new Color(0.0f, 0.05f, 0.05f),
                    new Color(0.4f, 0.5f, 0.5f),
                    new Color(0.04f, 0.7f, 0.7f),
                    0.078125, reflectivity);
            case GREEN_RUBBER -> new Material(type,
                    new Color(0.0f, 0.05f, 0.0f),
                    new Color(0.4f, 0.5f, 0.4f),
                    new Color(0.04f, 0.7f, 0.04f),
                    0.078125, reflectivity);
            case RED_RUBBER -> new Material(type,
                    new Color(0.05f, 0.0f, 0.0f),
                    new Color(0.5f, 0.4f, 0.4f),
                    new Color(0.7f, 0.04f, 0.04f),
                    0.078125, reflectivity);
            case WHITE_RUBBER -> new Material(type,
                    new Color(0.05f, 0.05f, 0.05f),
                    new Color(0.5f, 0.5f, 0.5f),
                    new Color(0.7f, 0.7f, 0.7f),
                    0.078125, reflectivity);
            case YELLOW_RUBBER -> new Material(type,
                    new Color(0.05f, 0.05f, 0.0f),
                    new Color(0.5f, 0.5f, 0.4f),
                    new Color(0.7f, 0.7f, 0.04f),
                    0.078125, reflectivity);
        };
    }
}