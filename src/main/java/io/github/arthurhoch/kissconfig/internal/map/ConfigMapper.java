package io.github.arthurhoch.kissconfig.internal.map;

import io.github.arthurhoch.kissconfig.ConfigLocationType;
import io.github.arthurhoch.kissconfig.ConfigMap;
import io.github.arthurhoch.kissconfig.ConfigValue;
import io.github.arthurhoch.kissconfig.annotations.ConfigName;
import io.github.arthurhoch.kissconfig.annotations.DefaultValue;
import io.github.arthurhoch.kissconfig.annotations.Required;
import io.github.arthurhoch.kissconfig.annotations.Secret;
import io.github.arthurhoch.kissconfig.exceptions.ConfigMappingException;
import io.github.arthurhoch.kissconfig.exceptions.ConfigMissingPropertyException;
import io.github.arthurhoch.kissconfig.internal.convert.TypeConverter;
import io.github.arthurhoch.kissconfig.internal.interpolate.VariableInterpolator;
import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Internal record mapper.
 */
public final class ConfigMapper {
    private ConfigMapper() {
    }

    public static <T> MappingResult<T> map(ConfigMap configMap, Class<T> targetType) {
        Set<String> secretKeys = new LinkedHashSet<>();
        Object value = mapRecord(configMap, targetType, "", targetType.getSimpleName(), secretKeys);
        return new MappingResult<>(targetType.cast(value), Set.copyOf(secretKeys));
    }

    private static Object mapRecord(
            ConfigMap configMap,
            Class<?> recordType,
            String prefix,
            String targetPath,
            Set<String> secretKeys
    ) {
        if (!recordType.isRecord()) {
            throw new ConfigMappingException(
                    "Cannot map configuration to " + recordType.getName() + ". Target type must be a Java record.",
                    null,
                    null,
                    recordType.getName(),
                    null
            );
        }

        RecordComponent[] components = recordType.getRecordComponents();
        Object[] args = new Object[components.length];
        Class<?>[] constructorTypes = Arrays.stream(components).map(RecordComponent::getType).toArray(Class[]::new);

        for (int i = 0; i < components.length; i++) {
            RecordComponent component = components[i];
            String key = keyFor(prefix, component);
            String componentTarget = targetPath + "." + component.getName();
            boolean required = component.isAnnotationPresent(Required.class);
            boolean secret = component.isAnnotationPresent(Secret.class);

            if (component.getType().isRecord()) {
                if (secret) {
                    markPrefixSecret(configMap, key, secretKeys);
                }
                if (hasPrefix(configMap, key)) {
                    args[i] = mapRecord(configMap, component.getType(), key, componentTarget, secretKeys);
                } else if (required) {
                    throw missing(key, componentTarget);
                } else {
                    args[i] = null;
                }
                continue;
            }

            ConfigValue sourceValue = configMap.getValue(key).orElse(null);
            if (sourceValue == null) {
                DefaultValue defaultValue = component.getAnnotation(DefaultValue.class);
                if (defaultValue != null) {
                    ConfigValue defaultSource = new ConfigValue(
                            key,
                            key,
                            defaultValue.value(),
                            "@DefaultValue(" + componentTarget + ")",
                            ConfigLocationType.EXPLICIT_FILE,
                            null,
                            null,
                            secret
                    );
                    String interpolated = VariableInterpolator.interpolateLiteral(defaultValue.value(), configMap, defaultSource);
                    sourceValue = defaultSource.withValue(interpolated);
                } else if (component.getType().isPrimitive() || required) {
                    throw missing(key, componentTarget);
                } else {
                    args[i] = null;
                    continue;
                }
            }

            if (secret) {
                sourceValue = sourceValue.withSecret(true);
                secretKeys.add(key);
            }
            args[i] = TypeConverter.convert(sourceValue.value(), component.getGenericType(), component.getType(), key, sourceValue);
        }

        try {
            Constructor<?> constructor = recordType.getDeclaredConstructor(constructorTypes);
            constructor.setAccessible(true);
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new ConfigMappingException(
                    "Cannot instantiate record '" + recordType.getName() + "': " + e.getMessage(),
                    e,
                    prefix,
                    null,
                    recordType.getName(),
                    null
            );
        }
    }

    private static String keyFor(String prefix, RecordComponent component) {
        ConfigName configName = component.getAnnotation(ConfigName.class);
        if (configName != null && configName.value().contains(".")) {
            return KeyNormalizer.normalize(configName.value());
        }
        String segment = configName == null
                ? KeyNormalizer.normalizeComponentName(component.getName())
                : KeyNormalizer.normalizeComponentName(configName.value());
        return KeyNormalizer.join(prefix, segment);
    }

    private static boolean hasPrefix(ConfigMap configMap, String prefix) {
        String normalizedPrefix = KeyNormalizer.normalize(prefix);
        return configMap.valuesByKey().keySet().stream()
                .anyMatch(key -> key.equals(normalizedPrefix) || key.startsWith(normalizedPrefix + "."));
    }

    private static void markPrefixSecret(ConfigMap configMap, String prefix, Set<String> secretKeys) {
        String normalizedPrefix = KeyNormalizer.normalize(prefix);
        configMap.valuesByKey().keySet().stream()
                .filter(key -> key.equals(normalizedPrefix) || key.startsWith(normalizedPrefix + "."))
                .forEach(secretKeys::add);
    }

    private static ConfigMissingPropertyException missing(String key, String targetPath) {
        return new ConfigMissingPropertyException(
                "Missing required configuration property '" + key + "' for target " + targetPath + ".",
                key,
                targetPath
        );
    }
}
