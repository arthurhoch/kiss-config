package io.github.arthurhoch.kissconfig;

/**
 * Identifies the kind of place KissConfig can read configuration from.
 */
public enum ConfigLocationType {
    /** Application classpath resources such as application.properties. */
    CLASSPATH,
    /** Library defaults from META-INF/kiss-config/defaults.properties. */
    CLASSPATH_LIBRARIES,
    /** Directory containing the running application JAR when available. */
    JAR_DIRECTORY,
    /** Process working directory. */
    WORKING_DIRECTORY,
    /** Explicit directory path. */
    EXPLICIT_PATH,
    /** Explicit single file path. */
    EXPLICIT_FILE,
    /** Explicit env file path or builder-configured env file placeholder. */
    ENV_FILE,
    /** Java system properties from System.getProperties(). */
    SYSTEM_PROPERTIES,
    /** Operating-system environment variables from System.getenv(). */
    ENVIRONMENT_VARIABLES
}
