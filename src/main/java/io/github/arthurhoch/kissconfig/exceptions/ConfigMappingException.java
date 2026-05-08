package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when configuration cannot be mapped to a target type.
 */
public class ConfigMappingException extends ConfigException {
    /**
     * Creates a mapping exception.
     *
     * @param message message
     * @param cause cause
     * @param key key
     * @param source source
     * @param targetType target type
     * @param safeValue safe value
     */
    public ConfigMappingException(
            String message,
            Throwable cause,
            String key,
            String source,
            String targetType,
            String safeValue
    ) {
        super(message, cause, key, source, targetType, safeValue);
    }

    /**
     * Creates a mapping exception without cause.
     *
     * @param message message
     * @param key key
     * @param source source
     * @param targetType target type
     * @param safeValue safe value
     */
    public ConfigMappingException(String message, String key, String source, String targetType, String safeValue) {
        this(message, null, key, source, targetType, safeValue);
    }
}
