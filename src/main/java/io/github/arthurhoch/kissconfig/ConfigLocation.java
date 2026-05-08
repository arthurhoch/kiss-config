package io.github.arthurhoch.kissconfig;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Immutable value object describing one configuration source location in a {@link SearchOrder}.
 *
 * <p>{@link #path(String)} means a directory, {@link #file(String)} means one explicit file, and
 * {@link #envFile(String)} means one explicit env file. Env files are never discovered unless
 * env loading is configured. See {@code docs/config-locations.md} and {@code docs/env-files.md}
 * for source-resolution details.</p>
 */
public final class ConfigLocation {
    private final ConfigLocationType type;
    private final Path path;

    private ConfigLocation(ConfigLocationType type, Path path) {
        this.type = Objects.requireNonNull(type, "type");
        this.path = path;
    }

    /**
     * Creates a location that reads application classpath property resources.
     *
     * <p>With an active profile, the profile property resource is attempted after the base
     * property resource.</p>
     *
     * @return a classpath location
     */
    public static ConfigLocation classpath() {
        return new ConfigLocation(ConfigLocationType.CLASSPATH, null);
    }

    /**
     * Creates a location that reads library defaults from META-INF/kiss-config/defaults.properties.
     *
     * <p>This location does not scan arbitrary dependency {@code application.properties}
     * resources.</p>
     *
     * @return a classpath library defaults location
     */
    public static ConfigLocation classpathLibraries() {
        return new ConfigLocation(ConfigLocationType.CLASSPATH_LIBRARIES, null);
    }

    /**
     * Creates a location that reads from the directory containing the running application JAR when available.
     *
     * <p>When the process is not running from a JAR, this location is skipped and reported.</p>
     *
     * @return a JAR directory location
     */
    public static ConfigLocation jarDirectory() {
        return new ConfigLocation(ConfigLocationType.JAR_DIRECTORY, null);
    }

    /**
     * Creates a location that reads from the process working directory.
     *
     * @return a working directory location
     */
    public static ConfigLocation workingDirectory() {
        return new ConfigLocation(ConfigLocationType.WORKING_DIRECTORY, null);
    }

    /**
     * Creates a location that treats the value as a directory.
     *
     * <p>Directory locations load property files and profile property files. They load env files
     * only when env loading is configured on the builder.</p>
     *
     * @param directory directory path
     * @return an explicit directory location
     */
    public static ConfigLocation path(String directory) {
        return path(Path.of(directory));
    }

    /**
     * Creates a location that treats the value as a directory.
     *
     * <p>Directory locations load property files and profile property files. They load env files
     * only when env loading is configured on the builder.</p>
     *
     * @param directory directory path
     * @return an explicit directory location
     */
    public static ConfigLocation path(Path directory) {
        return new ConfigLocation(ConfigLocationType.EXPLICIT_PATH, Objects.requireNonNull(directory, "directory"));
    }

    /**
     * Creates a location that treats the value as one explicit file.
     *
     * <p>Missing explicit files fail unless
     * {@link KissConfigBuilder#ignoreMissingExplicitFiles(boolean)} is enabled.</p>
     *
     * @param file file path
     * @return an explicit file location
     */
    public static ConfigLocation file(String file) {
        return file(Path.of(file));
    }

    /**
     * Creates a location that treats the value as one explicit file.
     *
     * <p>Missing explicit files fail unless
     * {@link KissConfigBuilder#ignoreMissingExplicitFiles(boolean)} is enabled.</p>
     *
     * @param file file path
     * @return an explicit file location
     */
    public static ConfigLocation file(Path file) {
        return new ConfigLocation(ConfigLocationType.EXPLICIT_FILE, Objects.requireNonNull(file, "file"));
    }

    /**
     * Creates a placeholder location for the env file configured on {@link KissConfigBuilder#envFile(String)}.
     *
     * <p>If the builder has no env file configured, this location is skipped and reported. With
     * an active profile, the builder-configured base env file is explicit and the generated
     * profile variant is optional.</p>
     *
     * @return a builder-configured env file location
     */
    public static ConfigLocation envFile() {
        return new ConfigLocation(ConfigLocationType.ENV_FILE, null);
    }

    /**
     * Creates a location that treats the value as one explicit env file.
     *
     * <p>This loads exactly the configured file path. Profile variants are not added
     * automatically for explicit env paths.</p>
     *
     * @param file env file path
     * @return an explicit env file location
     */
    public static ConfigLocation envFile(String file) {
        return envFile(Path.of(file));
    }

    /**
     * Creates a location that treats the value as one explicit env file.
     *
     * <p>This loads exactly the configured file path. Profile variants are not added
     * automatically for explicit env paths.</p>
     *
     * @param file env file path
     * @return an explicit env file location
     */
    public static ConfigLocation envFile(Path file) {
        return new ConfigLocation(ConfigLocationType.ENV_FILE, Objects.requireNonNull(file, "file"));
    }

    /**
     * Creates a location backed by Java system properties.
     *
     * @return a system properties location
     */
    public static ConfigLocation systemProperties() {
        return new ConfigLocation(ConfigLocationType.SYSTEM_PROPERTIES, null);
    }

    /**
     * Creates a location backed by operating-system environment variables.
     *
     * @return an environment variables location
     */
    public static ConfigLocation environmentVariables() {
        return new ConfigLocation(ConfigLocationType.ENVIRONMENT_VARIABLES, null);
    }

    /**
     * Returns the location type.
     *
     * @return location type
     */
    public ConfigLocationType type() {
        return type;
    }

    /**
     * Returns the configured path when this location has one.
     *
     * @return optional path
     */
    public Optional<Path> path() {
        return Optional.ofNullable(path);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ConfigLocation that)) {
            return false;
        }
        return type == that.type && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, path);
    }

    @Override
    public String toString() {
        return path == null ? type.name() : type.name() + "(" + path + ")";
    }
}
