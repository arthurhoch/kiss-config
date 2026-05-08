package io.github.arthurhoch.kissconfig.internal.map;

import java.util.Set;

/**
 * Internal mapper result.
 */
public record MappingResult<T>(T value, Set<String> secretKeys) {
}
