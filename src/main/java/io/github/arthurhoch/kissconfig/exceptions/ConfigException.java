package io.github.arthurhoch.kissconfig.exceptions;

import java.util.Optional;

/**
 * Base runtime exception for all KissConfig failures.
 */
public class ConfigException extends RuntimeException {
    private final String key;
    private final String source;
    private final String targetType;
    private final String safeValue;

    /**
     * Creates an exception with a message.
     *
     * @param message error message
     */
    public ConfigException(String message) {
        this(message, null, null, null, null, null);
    }

    /**
     * Creates an exception with a message and cause.
     *
     * @param message error message
     * @param cause cause
     */
    public ConfigException(String message, Throwable cause) {
        this(message, cause, null, null, null, null);
    }

    /**
     * Creates an exception with diagnostic metadata.
     *
     * @param message error message
     * @param cause cause
     * @param key related key
     * @param source source name or path
     * @param targetType target type
     * @param safeValue value safe for display
     */
    public ConfigException(String message, Throwable cause, String key, String source, String targetType, String safeValue) {
        super(message, cause);
        this.key = key;
        this.source = source;
        this.targetType = targetType;
        this.safeValue = safeValue;
    }

    /**
     * Returns the related key when available.
     *
     * @return optional key
     */
    public Optional<String> key() {
        return Optional.ofNullable(key);
    }

    /**
     * Returns the related source when available.
     *
     * @return optional source
     */
    public Optional<String> source() {
        return Optional.ofNullable(source);
    }

    /**
     * Returns the target type when available.
     *
     * @return optional target type
     */
    public Optional<String> targetType() {
        return Optional.ofNullable(targetType);
    }

    /**
     * Returns the safe display value when available.
     *
     * @return optional safe value
     */
    public Optional<String> safeValue() {
        return Optional.ofNullable(safeValue);
    }
}
