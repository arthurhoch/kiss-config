package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when a profile name is invalid.
 */
public class ConfigInvalidProfileException extends ConfigException {
    /**
     * Creates an invalid profile exception.
     *
     * @param message message
     */
    public ConfigInvalidProfileException(String message) {
        super(message);
    }
}
