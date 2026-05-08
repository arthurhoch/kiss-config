package io.github.arthurhoch.kissconfig;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * One effective or loaded configuration value with source metadata.
 *
 * @param canonicalKey normalized dot-form key
 * @param rawKey original key from the source
 * @param value raw or interpolated string value
 * @param sourceName human-readable source name
 * @param sourceType source location type
 * @param sourcePath source path when available
 * @param lineNumber source line number when available
 * @param secret whether this value should be masked in diagnostics
 */
public record ConfigValue(
        String canonicalKey,
        String rawKey,
        String value,
        String sourceName,
        ConfigLocationType sourceType,
        Path sourcePath,
        Integer lineNumber,
        boolean secret
) {
    /**
     * Creates a config value.
     */
    public ConfigValue {
        Objects.requireNonNull(canonicalKey, "canonicalKey");
        Objects.requireNonNull(rawKey, "rawKey");
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(sourceName, "sourceName");
        Objects.requireNonNull(sourceType, "sourceType");
    }

    /**
     * Returns the optional source path.
     *
     * @return optional path
     */
    public Optional<Path> optionalSourcePath() {
        return Optional.ofNullable(sourcePath);
    }

    /**
     * Returns the optional line number.
     *
     * @return optional line number
     */
    public Optional<Integer> optionalLineNumber() {
        return Optional.ofNullable(lineNumber);
    }

    /**
     * Returns this value with a different string value.
     *
     * @param newValue replacement value
     * @return copied config value
     */
    public ConfigValue withValue(String newValue) {
        return new ConfigValue(canonicalKey, rawKey, newValue, sourceName, sourceType, sourcePath, lineNumber, secret);
    }

    /**
     * Returns this value with updated secret status.
     *
     * @param newSecret secret status
     * @return copied config value
     */
    public ConfigValue withSecret(boolean newSecret) {
        return new ConfigValue(canonicalKey, rawKey, value, sourceName, sourceType, sourcePath, lineNumber, newSecret);
    }

    /**
     * Returns the value masked when secret.
     *
     * @return safe display value
     */
    public String displayValue() {
        return secret ? "******" : value;
    }
}
