package io.github.arthurhoch.kissconfig.internal.source;

import io.github.arthurhoch.kissconfig.ConfigMap;
import io.github.arthurhoch.kissconfig.LoadedConfigSource;

import java.util.List;

/**
 * Internal result of source loading and merging.
 */
public record LoadedConfigData(ConfigMap configMap, List<LoadedConfigSource> sources) {
}
