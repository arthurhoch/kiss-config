package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when duplicate canonical keys are not allowed by the active merge strategy.
 */
public class ConfigDuplicateKeyException extends ConfigException {
    /**
     * Creates a duplicate key exception.
     *
     * @param message message
     * @param key duplicate key
     * @param source source
     */
    public ConfigDuplicateKeyException(String message, String key, String source) {
        super(message, null, key, source, null, null);
    }
}
