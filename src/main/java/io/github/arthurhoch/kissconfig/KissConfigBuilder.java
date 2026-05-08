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
 *
 * <p>Defaults are {@code application.properties}, no env file, no profile,
 * {@link SearchOrder#defaults()}, and {@link MergeStrategy#FILL_MISSING_ONLY}. Env files are
 * opt-in through {@link #envFile(String)}, {@link #envFile(Path)}, or an explicit
 * {@link ConfigLocation#envFile(String)} in the configured search order. See
 * {@code docs/search-order.md}, {@code docs/profiles.md}, and {@code docs/env-files.md} for
 * detailed behavior.</p>
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
     * <p>With an active profile, directory and classpath locations also attempt a profile variant
     * such as {@code application-prod.properties} for profile {@code prod}.</p>
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
     * <p>With an active profile, directory and classpath locations also attempt a profile variant
     * such as {@code application-prod.properties} for profile {@code prod}.</p>
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
     * <p>Env files are not loaded by default. When this method is used, directory-based locations
     * attempt the configured env file and, when a profile is active, a profile variant. For
     * {@code .env} and profile {@code prod}, the profile variant is {@code .env.prod}.</p>
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
     * <p>Env files are not loaded by default. When this method is used, directory-based locations
     * attempt the configured env file and, when a profile is active, a profile variant. For
     * {@code myapp.env} and profile {@code prod}, the profile variant is
     * {@code myapp-prod.env}.</p>
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
     * <p>Profiles are validated against {@code [a-zA-Z0-9_-]+}. Profiles are not enabled
     * automatically by {@link SearchOrder#production()}.</p>
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
     * <p>{@link SearchOrder} is read order. Priority depends on the active
     * {@link MergeStrategy}.</p>
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
     * <p>The default is {@link MergeStrategy#FILL_MISSING_ONLY}. Override and duplicate-fail
     * behavior must be selected explicitly.</p>
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
     * <p>Auto-discovered missing files are skipped. Explicit files declared through
     * {@link ConfigLocation#file(String)} or {@link ConfigLocation#envFile(String)} fail when
     * missing unless this is set to {@code true}.</p>
     *
     * @param ignoreMissingExplicitFiles true to skip missing explicit files
     * @return this builder
     */
    public KissConfigBuilder ignoreMissingExplicitFiles(boolean ignoreMissingExplicitFiles) {
        this.ignoreMissingExplicitFiles = ignoreMissingExplicitFiles;
        return this;
    }

    /**
     * Loads and maps configuration to the target record type, returning only the mapped value.
     *
     * @param targetType target type
     * @param <T> target type
     * @return mapped configuration object
     */
    public <T> T mapTo(Class<T> targetType) {
        return load(targetType).value();
    }

    /**
     * Loads and maps configuration to the target record type, returning value, sources, map, and report.
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
