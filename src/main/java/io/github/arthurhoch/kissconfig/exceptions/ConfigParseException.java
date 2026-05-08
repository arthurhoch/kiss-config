package io.github.arthurhoch.kissconfig.exceptions;

/**
 * Thrown when a configuration source cannot be parsed.
 */
public class ConfigParseException extends ConfigException {
    /**
     * Creates a parse exception.
     *
     * @param message message
     * @param cause cause
     * @param source source
     */
    public ConfigParseException(String message, Throwable cause, String source) {
        super(message, cause, null, source, null, null);
    }
}
