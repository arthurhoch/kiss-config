package io.github.arthurhoch.kissconfig;

/**
 * Entry point for loading configuration into typed Java records.
 *
 * <p>The default load path uses {@link SearchOrder#defaults()}, the default property file
 * {@code application.properties}, no env file unless configured through the builder, and
 * {@link MergeStrategy#FILL_MISSING_ONLY}. See {@code docs/getting-started.md} and
 * {@code docs/api.md} for user-facing examples.</p>
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
     * Loads configuration into the target record type using default builder options.
     *
     * @param targetType target record type
     * @param <T> target type
     * @return mapped configuration object
     */
    public static <T> T load(Class<T> targetType) {
        return builder().mapTo(targetType);
    }
}
