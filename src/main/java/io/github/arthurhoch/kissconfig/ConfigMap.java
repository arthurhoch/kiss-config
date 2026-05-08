package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable normalized configuration map.
 */
public final class ConfigMap {
    private final Map<String, ConfigValue> values;

    /**
     * Creates a config map from normalized keys to config values.
     *
     * @param values values to copy
     */
    public ConfigMap(Map<String, ConfigValue> values) {
        this.values = Collections.unmodifiableMap(new LinkedHashMap<>(Objects.requireNonNull(values, "values")));
    }

    /**
     * Returns the string value for a key after normalizing the requested key.
     *
     * @param key raw or canonical key
     * @return optional string value
     */
    public Optional<String> get(String key) {
        return getValue(key).map(ConfigValue::value);
    }

    /**
     * Returns the full config value for a key after normalizing the requested key.
     *
     * @param key raw or canonical key
     * @return optional config value
     */
    public Optional<ConfigValue> getValue(String key) {
        return Optional.ofNullable(values.get(KeyNormalizer.normalize(key)));
    }

    /**
     * Returns true when the key exists after normalization.
     *
     * @param key raw or canonical key
     * @return whether the key exists
     */
    public boolean containsKey(String key) {
        return values.containsKey(KeyNormalizer.normalize(key));
    }

    /**
     * Returns immutable values keyed by canonical key.
     *
     * @return values by key
     */
    public Map<String, ConfigValue> valuesByKey() {
        return values;
    }

    /**
     * Returns a string-only view keyed by canonical key.
     *
     * @return string map
     */
    public Map<String, String> asMap() {
        Map<String, String> result = new LinkedHashMap<>();
        values.forEach((key, value) -> result.put(key, value.value()));
        return Collections.unmodifiableMap(result);
    }

    /**
     * Returns the config values in deterministic key order.
     *
     * @return config values
     */
    public Collection<ConfigValue> values() {
        return values.values();
    }

    /**
     * Returns a copy with additional keys marked as secret.
     *
     * @param secretKeys canonical or raw keys to mark secret
     * @return copied config map
     */
    public ConfigMap withSecretKeys(Set<String> secretKeys) {
        if (secretKeys.isEmpty()) {
            return this;
        }
        Map<String, ConfigValue> copy = new LinkedHashMap<>();
        Set<String> normalized = secretKeys.stream()
                .map(KeyNormalizer::normalize)
                .collect(java.util.stream.Collectors.toSet());
        values.forEach((key, value) -> copy.put(key, normalized.contains(key) ? value.withSecret(true) : value));
        return new ConfigMap(copy);
    }
}
