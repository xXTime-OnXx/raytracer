package ch.hslu.raytracer;

import java.io.File;

/**
 * Settings for the ray tracer renderer.
 */
public class RenderSettings {
    private final int width;
    private final int height;
    private final int maxReflectionDepth;
    private final int numThreads;
    private final String outputFilename;
    private final String outputFormat;

    private RenderSettings(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.maxReflectionDepth = builder.maxReflectionDepth;
        this.numThreads = builder.numThreads;
        this.outputFilename = builder.outputFilename;
        this.outputFormat = builder.outputFormat;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxReflectionDepth() {
        return maxReflectionDepth;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public File getOutputFile() {
        return new File(outputFilename + "." + outputFormat);
    }

    /**
     * Creates default render settings.
     */
    public static RenderSettings createDefault() {
        return new Builder().build();
    }

    /**
     * Builder for RenderSettings.
     */
    public static class Builder {
        private int width = 1920;
        private int height = 1080;
        private int maxReflectionDepth = 10;
        private int numThreads = Runtime.getRuntime().availableProcessors();
        private String outputFilename = "raytraced_image";
        private String outputFormat = "png";

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder maxReflectionDepth(int maxReflectionDepth) {
            this.maxReflectionDepth = maxReflectionDepth;
            return this;
        }

        public Builder numThreads(int numThreads) {
            this.numThreads = numThreads;
            return this;
        }

        public Builder outputFilename(String outputFilename) {
            this.outputFilename = outputFilename;
            return this;
        }

        public Builder outputFormat(String outputFormat) {
            this.outputFormat = outputFormat;
            return this;
        }

        public RenderSettings build() {
            return new RenderSettings(this);
        }
    }
}