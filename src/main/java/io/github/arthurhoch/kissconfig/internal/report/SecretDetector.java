package io.github.arthurhoch.kissconfig.internal.report;

import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;

import java.util.Set;

/**
 * Internal secret-looking key detector.
 */
public final class SecretDetector {
    private static final Set<String> SECRET_SEGMENTS = Set.of(
            "password",
            "passwd",
            "secret",
            "token",
            "credential"
    );

    private SecretDetector() {
    }

    public static boolean isSecretKey(String key) {
        String normalized = KeyNormalizer.normalize(key);
        if (normalized.isBlank()) {
            return false;
        }
        if (normalized.equals("api.key") || normalized.endsWith(".api.key")) {
            return true;
        }
        if (normalized.equals("private.key") || normalized.endsWith(".private.key")) {
            return true;
        }
        for (String segment : normalized.split("\\.")) {
            if (SECRET_SEGMENTS.contains(segment)) {
                return true;
            }
        }
        return false;
    }

    public static String maskIfSecret(String key, String value) {
        return isSecretKey(key) ? "******" : value;
    }
}
