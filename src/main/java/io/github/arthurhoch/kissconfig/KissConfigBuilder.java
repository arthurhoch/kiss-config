package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.exceptions.ConfigInvalidProfileException;
import io.github.arthurhoch.kissconfig.internal.interpolate.VariableInterpolator;
import io.github.arthurhoch.kissconfig.internal.map.ConfigMapper;
import io.github.arthurhoch.kissconfig.internal.map.MappingResult;
import io.github.arthurhoch.kissconfig.internal.report.ConfigReportRenderer;
import io.github.arthurhoch.kissconfig.internal.source.ConfigSourceLoader;
import io.github.arthurhoch.kissconfig.internal.source.LoadOptions;
import io.github.arthurhoch.kissconfig.internal.source.LoadedConfigData;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Builder for explicit KissConfig loading.
 */
public final class KissConfigBuilder {
    private static final Pattern PROFILE_PATTERN = Pattern.compile("[a-zA-Z0-9_-]+");

    private Path propertyFile = Path.of("application.properties");
    private Path envFile;
    private String profile;
    private SearchOrder searchOrder = SearchOrder.defaults();
    private MergeStrategy mergeStrategy = MergeStrategy.FILL_MISSING_ONLY;
    private boolean ignoreMissingExplicitFiles;
    private Map<String, String> systemProperties;
    private Map<String, String> environmentVariables;
    private ClassLoader classLoader;

    KissConfigBuilder() {
    }

    /**
     * Sets the property file name or path used by compatible locations.
     *
     * @param propertyFile property file
     * @return this builder
     */
    public KissConfigBuilder propertyFile(String propertyFile) {
        return propertyFile(Path.of(propertyFile));
    }

    /**
     * Sets the property file name or path used by compatible locations.
     *
     * @param propertyFile property file
     * @return this builder
     */
    public KissConfigBuilder propertyFile(Path propertyFile) {
        this.propertyFile = Objects.requireNonNull(propertyFile, "propertyFile");
        return this;
    }

    /**
     * Configures env file loading.
     *
     * @param envFile env file
     * @return this builder
     */
    public KissConfigBuilder envFile(String envFile) {
        return envFile(Path.of(envFile));
    }

    /**
     * Configures env file loading.
     *
     * @param envFile env file
     * @return this builder
     */
    public KissConfigBuilder envFile(Path envFile) {
        this.envFile = Objects.requireNonNull(envFile, "envFile");
        return this;
    }

    /**
     * Sets the active profile.
     *
     * @param profile profile name matching [a-zA-Z0-9_-]+
     * @return this builder
     */
    public KissConfigBuilder profile(String profile) {
        if (profile == null || profile.isBlank() || !PROFILE_PATTERN.matcher(profile).matches()) {
            throw new ConfigInvalidProfileException(
                    "Invalid profile '" + profile + "'. Profile names must match [a-zA-Z0-9_-]+."
            );
        }
        this.profile = profile;
        return this;
    }

    /**
     * Sets the source search order.
     *
     * @param searchOrder search order
     * @return this builder
     */
    public KissConfigBuilder searchOrder(SearchOrder searchOrder) {
        this.searchOrder = Objects.requireNonNull(searchOrder, "searchOrder");
        return this;
    }

    /**
     * Sets the merge strategy.
     *
     * @param mergeStrategy merge strategy
     * @return this builder
     */
    public KissConfigBuilder mergeStrategy(MergeStrategy mergeStrategy) {
        this.mergeStrategy = Objects.requireNonNull(mergeStrategy, "mergeStrategy");
        return this;
    }

    /**
     * Controls missing explicit file behavior.
     *
     * @param ignoreMissingExplicitFiles true to skip missing explicit files
     * @return this builder
     */
    public KissConfigBuilder ignoreMissingExplicitFiles(boolean ignoreMissingExplicitFiles) {
        this.ignoreMissingExplicitFiles = ignoreMissingExplicitFiles;
        return this;
    }

    /**
     * Loads and maps configuration to the target type, returning only the mapped value.
     *
     * @param targetType target type
     * @param <T> target type
     * @return mapped configuration object
     */
    public <T> T mapTo(Class<T> targetType) {
        return load(targetType).value();
    }

    /**
     * Loads and maps configuration to the target type, returning value, sources, map, and report.
     *
     * @param targetType target type
     * @param <T> target type
     * @return load result
     */
    public <T> KissConfigResult<T> load(Class<T> targetType) {
        Objects.requireNonNull(targetType, "targetType");
        LoadOptions options = options();
        LoadedConfigData loaded = ConfigSourceLoader.load(options);
        ConfigMap interpolated = VariableInterpolator.interpolate(loaded.configMap());
        MappingResult<T> mapped = ConfigMapper.map(interpolated, targetType);
        ConfigMap finalMap = interpolated.withSecretKeys(mapped.secretKeys());
        String report = ConfigReportRenderer.render(options, finalMap, loaded.sources());
        return new KissConfigResult<>(mapped.value(), finalMap, loaded.sources(), report);
    }

    KissConfigBuilder systemProperties(Map<String, String> systemProperties) {
        this.systemProperties = Map.copyOf(systemProperties);
        return this;
    }

    KissConfigBuilder environmentVariables(Map<String, String> environmentVariables) {
        this.environmentVariables = Map.copyOf(environmentVariables);
        return this;
    }

    KissConfigBuilder classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    private LoadOptions options() {
        return new LoadOptions(
                propertyFile,
                envFile,
                profile,
                searchOrder,
                mergeStrategy,
                ignoreMissingExplicitFiles,
                systemProperties == null ? currentSystemProperties() : systemProperties,
                environmentVariables == null ? System.getenv() : environmentVariables,
                classLoader == null ? defaultClassLoader() : classLoader
        );
    }

    private static Map<String, String> currentSystemProperties() {
        Properties properties = System.getProperties();
        Map<String, String> result = new LinkedHashMap<>();
        properties.stringPropertyNames().stream().sorted().forEach(name -> result.put(name, properties.getProperty(name)));
        return result;
    }

    private static ClassLoader defaultClassLoader() {
        ClassLoader context = Thread.currentThread().getContextClassLoader();
        return context == null ? KissConfig.class.getClassLoader() : context;
    }
}
