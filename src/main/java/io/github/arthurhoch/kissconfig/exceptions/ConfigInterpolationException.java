package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when variable interpolation fails.
 */
public class ConfigInterpolationException extends ConfigException {
    /**
     * Creates an interpolation exception.
     *
     * @param message message
     * @param key key
     * @param source source
     * @param safeValue safe value
     */
    public ConfigInterpolationException(String message, String key, String source, String safeValue) {
        super(message, null, key, source, null, safeValue);
    }
}
