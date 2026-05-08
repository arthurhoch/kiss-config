package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when a required configuration key is missing.
 */
public class ConfigMissingPropertyException extends ConfigMappingException {
    /**
     * Creates a missing property exception.
     *
     * @param message message
     * @param key missing key
     * @param targetType target type
     */
    public ConfigMissingPropertyException(String message, String key, String targetType) {
        super(message, key, null, targetType, null);
    }
}
