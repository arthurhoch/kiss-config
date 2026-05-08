package io.github.arthurhoch.kissconfig;

/**
 * Entry point for loading configuration.
 */
public final class KissConfig {
    private KissConfig() {
    }

    /**
     * Creates a new builder with default options.
     *
     * @return builder
     */
    public static KissConfigBuilder builder() {
        return new KissConfigBuilder();
    }

    /**
     * Loads configuration into the target type using default builder options.
     *
     * @param targetType target type
     * @param <T> target type
     * @return mapped configuration object
     */
    public static <T> T load(Class<T> targetType) {
        return builder().mapTo(targetType);
    }
}
