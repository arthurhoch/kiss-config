package io.github.arthurhoch.kissconfig.internal.source;

import io.github.arthurhoch.kissconfig.MergeStrategy;
import io.github.arthurhoch.kissconfig.SearchOrder;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * Internal immutable loader options.
 */
public record LoadOptions(
        Path propertyFile,
        Path envFile,
        String profile,
        SearchOrder searchOrder,
        MergeStrategy mergeStrategy,
        boolean ignoreMissingExplicitFiles,
        Map<String, String> systemProperties,
        Map<String, String> environmentVariables,
        ClassLoader classLoader
) {
    public Optional<Path> optionalEnvFile() {
        return Optional.ofNullable(envFile);
    }

    public Optional<String> optionalProfile() {
        return Optional.ofNullable(profile);
    }
}
