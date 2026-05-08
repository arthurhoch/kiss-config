package io.github.arthurhoch.kissconfig.internal.normalize;

import java.util.Locale;

/**
 * Internal key normalization utilities.
 */
public final class KeyNormalizer {
    private KeyNormalizer() {
    }

    public static String normalize(String key) {
        if (key == null) {
            return "";
        }
        String normalized = key.trim()
                .toLowerCase(Locale.ROOT)
                .replace('_', '.')
                .replace('-', '.');
        normalized = normalized.replaceAll("\\.+", ".");
        normalized = normalized.replaceAll("^\\.+", "");
        normalized = normalized.replaceAll("\\.+$", "");
        return normalized;
    }

    public static String normalizeComponentName(String componentName) {
        if (componentName == null || componentName.isBlank()) {
            return "";
        }
        String dotted = componentName.replaceAll("([a-z0-9])([A-Z])", "$1.$2");
        return normalize(dotted);
    }

    public static String join(String prefix, String segment) {
        String normalizedSegment = normalize(segment);
        if (prefix == null || prefix.isBlank()) {
            return normalizedSegment;
        }
        if (normalizedSegment.isBlank()) {
            return normalize(prefix);
        }
        return normalize(prefix) + "." + normalizedSegment;
    }
}
