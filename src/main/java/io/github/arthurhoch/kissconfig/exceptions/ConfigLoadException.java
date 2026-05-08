package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when a configuration source cannot be loaded.
 */
public class ConfigLoadException extends ConfigException {
    /**
     * Creates a load exception.
     *
     * @param message message
     */
    public ConfigLoadException(String message) {
        super(message);
    }

    /**
     * Creates a load exception with a cause and source.
     *
     * @param message message
     * @param cause cause
     * @param source source
     */
    public ConfigLoadException(String message, Throwable cause, String source) {
        super(message, cause, null, source, null, null);
    }
}
