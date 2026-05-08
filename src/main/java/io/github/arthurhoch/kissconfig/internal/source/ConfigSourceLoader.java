package io.github.arthurhoch.kissconfig.internal.source;

import io.github.arthurhoch.kissconfig.ConfigMap;
import io.github.arthurhoch.kissconfig.ConfigValue;
import io.github.arthurhoch.kissconfig.LoadedConfigSource;
import io.github.arthurhoch.kissconfig.MergeStrategy;
import io.github.arthurhoch.kissconfig.exceptions.ConfigDuplicateKeyException;
import io.github.arthurhoch.kissconfig.exceptions.ConfigLoadException;
import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;
import io.github.arthurhoch.kissconfig.internal.parse.EnvConfigParser;
import io.github.arthurhoch.kissconfig.internal.parse.ParsedConfigEntry;
import io.github.arthurhoch.kissconfig.internal.parse.PropertiesConfigParser;
import io.github.arthurhoch.kissconfig.internal.report.SecretDetector;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Loads, normalizes, and merges resolved source candidates.
 */
public final class ConfigSourceLoader {
    private ConfigSourceLoader() {
    }

    public static LoadedConfigData load(LoadOptions options) {
        List<SourceCandidate> candidates = ConfigSourceResolver.resolve(options);
        List<LoadedConfigSource> sourceReports = new ArrayList<>();
        Map<String, ConfigValue> merged = new LinkedHashMap<>();
        Set<String> seenSourceIds = new TreeSet<>();

        for (SourceCandidate candidate : candidates) {
            if (candidate.preSkipped()) {
                sourceReports.add(skipped(candidate, candidate.skipMessage()));
                continue;
            }

            String sourceId = sourceId(candidate);
            if (sourceId != null && !seenSourceIds.add(sourceId)) {
                sourceReports.add(skipped(candidate, "Skipped duplicate resolved source"));
                continue;
            }

            if (candidate.map() != null) {
                List<ConfigValue> values = mapValues(candidate);
                mergeValues(merged, values, options.mergeStrategy());
                sourceReports.add(loaded(candidate, values.size()));
                continue;
            }

            if (candidate.url() == null && candidate.path() == null) {
                sourceReports.add(skipped(candidate, "Skipped missing optional classpath resource"));
                continue;
            }

            if (candidate.path() != null && !Files.isRegularFile(candidate.path())) {
                if (candidate.explicit() && !options.ignoreMissingExplicitFiles()) {
                    throw new ConfigLoadException(
                            "Cannot load explicit file '" + candidate.path() + "': file does not exist. "
                                    + "Set ignoreMissingExplicitFiles(true) to skip missing explicit files.",
                            null,
                            candidate.path().toString()
                    );
                }
                sourceReports.add(skipped(candidate, "Skipped missing optional source"));
                continue;
            }

            List<ConfigValue> values = fileOrUrlValues(candidate);
            mergeValues(merged, values, options.mergeStrategy());
            sourceReports.add(loaded(candidate, values.size()));
        }

        return new LoadedConfigData(new ConfigMap(merged), sourceReports);
    }

    private static List<ConfigValue> fileOrUrlValues(SourceCandidate candidate) {
        try (InputStream inputStream = candidate.path() != null
                ? Files.newInputStream(candidate.path())
                : candidate.url().openStream()) {
            List<ParsedConfigEntry> entries = parse(candidate, inputStream);
            return toConfigValues(candidate, entries);
        } catch (ConfigLoadException e) {
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigLoadException(
                    "Cannot load source '" + candidate.name() + "': " + e.getMessage(),
                    e,
                    candidate.name()
            );
        }
    }

    private static List<ParsedConfigEntry> parse(SourceCandidate candidate, InputStream inputStream) {
        return switch (candidate.format()) {
            case PROPERTIES -> PropertiesConfigParser.parse(inputStream, candidate.name());
            case ENV -> EnvConfigParser.parse(inputStream, candidate.name());
            case MAP -> throw new IllegalStateException("Map sources are not parsed from streams");
        };
    }

    private static List<ConfigValue> mapValues(SourceCandidate candidate) {
        Map<String, String> sorted = new TreeMap<>(candidate.map());
        List<ParsedConfigEntry> entries = sorted.entrySet().stream()
                .map(entry -> new ParsedConfigEntry(entry.getKey(), entry.getValue(), null))
                .toList();
        return toConfigValues(candidate, entries);
    }

    private static List<ConfigValue> toConfigValues(SourceCandidate candidate, List<ParsedConfigEntry> entries) {
        List<ConfigValue> values = new ArrayList<>();
        for (ParsedConfigEntry entry : entries) {
            String canonicalKey = KeyNormalizer.normalize(entry.rawKey());
            if (canonicalKey.isBlank()) {
                continue;
            }
            values.add(new ConfigValue(
                    canonicalKey,
                    entry.rawKey(),
                    entry.value(),
                    candidate.name(),
                    candidate.type(),
                    candidate.path(),
                    entry.lineNumber(),
                    SecretDetector.isSecretKey(canonicalKey)
            ));
        }
        values.sort(Comparator.comparing(ConfigValue::canonicalKey).thenComparing(ConfigValue::rawKey));
        return values;
    }

    private static void mergeValues(Map<String, ConfigValue> merged, List<ConfigValue> values, MergeStrategy strategy) {
        for (ConfigValue value : values) {
            ConfigValue existing = merged.get(value.canonicalKey());
            if (existing == null) {
                merged.put(value.canonicalKey(), value);
                continue;
            }
            switch (strategy) {
                case FILL_MISSING_ONLY -> {
                }
                case OVERRIDE_EXISTING -> merged.put(value.canonicalKey(), value);
                case FAIL_ON_DUPLICATE -> throw new ConfigDuplicateKeyException(
                        "Duplicate configuration key '" + value.canonicalKey() + "' from source '" + value.sourceName()
                                + "'. Existing value came from '" + existing.sourceName() + "'.",
                        value.canonicalKey(),
                        value.sourceName()
                );
            }
        }
    }

    private static LoadedConfigSource loaded(SourceCandidate candidate, int count) {
        return new LoadedConfigSource(
                candidate.name(),
                candidate.type(),
                candidate.path(),
                true,
                candidate.optional(),
                candidate.explicit(),
                count,
                "Loaded"
        );
    }

    private static LoadedConfigSource skipped(SourceCandidate candidate, String message) {
        return new LoadedConfigSource(
                candidate.name(),
                candidate.type(),
                candidate.path(),
                false,
                candidate.optional(),
                candidate.explicit(),
                0,
                message
        );
    }

    private static String sourceId(SourceCandidate candidate) {
        try {
            if (candidate.path() != null) {
                Path path = candidate.path();
                if (Files.exists(path)) {
                    return path.toRealPath().toString();
                }
                return path.toAbsolutePath().normalize().toString();
            }
            if (candidate.url() != null) {
                return candidate.url().toExternalForm();
            }
            return null;
        } catch (Exception e) {
            return candidate.path().toAbsolutePath().normalize().toString();
        }
    }
}
