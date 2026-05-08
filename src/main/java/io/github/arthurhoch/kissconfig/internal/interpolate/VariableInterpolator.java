package io.github.arthurhoch.kissconfig.internal.interpolate;

import io.github.arthurhoch.kissconfig.ConfigMap;
import io.github.arthurhoch.kissconfig.ConfigValue;
import io.github.arthurhoch.kissconfig.exceptions.ConfigInterpolationException;
import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Interpolates ${NAME} and ${NAME:default} expressions.
 */
public final class VariableInterpolator {
    private VariableInterpolator() {
    }

    public static ConfigMap interpolate(ConfigMap configMap) {
        Map<String, ConfigValue> input = configMap.valuesByKey();
        Map<String, String> rawIndex = rawIndex(input);
        Map<String, String> resolvedValues = new LinkedHashMap<>();
        Map<String, ConfigValue> output = new LinkedHashMap<>();

        for (String key : input.keySet()) {
            String resolved = resolveKey(key, input, rawIndex, resolvedValues, new ArrayDeque<>());
            output.put(key, input.get(key).withValue(resolved));
        }

        return new ConfigMap(output);
    }

    public static String interpolateLiteral(String literal, ConfigMap configMap, ConfigValue sourceValue) {
        return interpolateValue(literal, sourceValue, configMap.valuesByKey(), rawIndex(configMap.valuesByKey()), new LinkedHashMap<>(), new ArrayDeque<>());
    }

    private static String resolveKey(
            String key,
            Map<String, ConfigValue> input,
            Map<String, String> rawIndex,
            Map<String, String> resolvedValues,
            Deque<String> chain
    ) {
        String canonicalKey = KeyNormalizer.normalize(key);
        if (resolvedValues.containsKey(canonicalKey)) {
            return resolvedValues.get(canonicalKey);
        }
        if (chain.contains(canonicalKey)) {
            throw cycle(input.get(canonicalKey), canonicalKey, chain);
        }
        ConfigValue value = input.get(canonicalKey);
        if (value == null) {
            return null;
        }
        chain.addLast(canonicalKey);
        String resolved = interpolateValue(value.value(), value, input, rawIndex, resolvedValues, chain);
        chain.removeLast();
        resolvedValues.put(canonicalKey, resolved);
        return resolved;
    }

    private static String interpolateValue(
            String value,
            ConfigValue sourceValue,
            Map<String, ConfigValue> input,
            Map<String, String> rawIndex,
            Map<String, String> resolvedValues,
            Deque<String> chain
    ) {
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < value.length()) {
            int start = value.indexOf("${", index);
            if (start < 0) {
                result.append(value.substring(index));
                break;
            }
            result.append(value, index, start);
            int end = value.indexOf('}', start + 2);
            if (end < 0) {
                throw interpolationError(sourceValue, "Unclosed interpolation expression in value for key '"
                        + sourceValue.canonicalKey() + "'.");
            }
            String expression = value.substring(start + 2, end);
            result.append(resolveExpression(expression, sourceValue, input, rawIndex, resolvedValues, chain));
            index = end + 1;
        }
        return result.toString();
    }

    private static String resolveExpression(
            String expression,
            ConfigValue sourceValue,
            Map<String, ConfigValue> input,
            Map<String, String> rawIndex,
            Map<String, String> resolvedValues,
            Deque<String> chain
    ) {
        int colon = expression.indexOf(':');
        String name = colon < 0 ? expression.trim() : expression.substring(0, colon).trim();
        String defaultValue = colon < 0 ? null : expression.substring(colon + 1);
        if (name.isEmpty()) {
            throw interpolationError(sourceValue, "Empty interpolation variable in key '" + sourceValue.canonicalKey() + "'.");
        }

        String canonical = KeyNormalizer.normalize(name);
        if (!input.containsKey(canonical)) {
            String rawMatch = rawIndex.get(name);
            if (rawMatch != null) {
                canonical = rawMatch;
            }
        }

        if (input.containsKey(canonical)) {
            return resolveKey(canonical, input, rawIndex, resolvedValues, chain);
        }

        if (defaultValue != null) {
            return interpolateValue(defaultValue, sourceValue, input, rawIndex, resolvedValues, chain);
        }

        throw interpolationError(
                sourceValue,
                "Missing interpolation variable '${" + expression + "}' for key '" + sourceValue.canonicalKey()
                        + "'. Dependency chain: " + chain
        );
    }

    private static Map<String, String> rawIndex(Map<String, ConfigValue> values) {
        Map<String, String> raw = new LinkedHashMap<>();
        values.values().forEach(value -> raw.putIfAbsent(value.rawKey(), value.canonicalKey()));
        return raw;
    }

    private static ConfigInterpolationException cycle(ConfigValue sourceValue, String key, Deque<String> chain) {
        String source = sourceValue == null ? null : sourceValue.sourceName();
        String safeValue = sourceValue == null ? null : sourceValue.displayValue();
        return new ConfigInterpolationException(
                "Circular interpolation reference for key '" + key + "'. Dependency chain: " + chain + " -> " + key,
                key,
                source,
                safeValue
        );
    }

    private static ConfigInterpolationException interpolationError(ConfigValue sourceValue, String message) {
        return new ConfigInterpolationException(
                message,
                sourceValue.canonicalKey(),
                sourceValue.sourceName(),
                sourceValue.displayValue()
        );
    }
}
