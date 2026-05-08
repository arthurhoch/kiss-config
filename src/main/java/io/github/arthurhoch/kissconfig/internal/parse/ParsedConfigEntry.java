package io.github.arthurhoch.kissconfig.internal.parse;

/**
 * Internal parser output before key normalization.
 */
public record ParsedConfigEntry(String rawKey, String value, Integer lineNumber) {
}
