package io.github.arthurhoch.kissconfig;

import java.util.List;
import java.util.Objects;

/**
 * Result returned when callers need the mapped value and diagnostic report.
 *
 * @param value mapped configuration object
 * @param configMap effective normalized configuration map
 * @param sources attempted and loaded sources
 * @param report human-readable report
 * @param <T> mapped type
 */
public record KissConfigResult<T>(
        T value,
        ConfigMap configMap,
        List<LoadedConfigSource> sources,
        String report
) {
    /**
     * Creates a result.
     */
    public KissConfigResult {
        Objects.requireNonNull(value, "value");
        Objects.requireNonNull(configMap, "configMap");
        sources = List.copyOf(Objects.requireNonNull(sources, "sources"));
        Objects.requireNonNull(report, "report");
    }
}
