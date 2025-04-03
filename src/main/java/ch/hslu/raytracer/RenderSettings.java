package ch.hslu.raytracer;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

import java.io.File;

@Getter
@Builder(toBuilder = true)
public class RenderSettings {

    @Default
    int width = 1920;
    @Default
    int height = 1080;
    @Default
    int maxReflectionDepth = 10;
    @Default
    int numThreads = Runtime.getRuntime().availableProcessors();
    @Default
    String outputFilename = "raytraced_image";
    @Default
    String outputFormat = "png";

    public File getOutputFile() {
        return new File(outputFilename + "." + outputFormat);
    }

    public static RenderSettings createDefault() {
        return builder().build();
    }
}
