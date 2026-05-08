package io.github.arthurhoch.kissconfig.internal.convert;

import io.github.arthurhoch.kissconfig.ConfigValue;
import io.github.arthurhoch.kissconfig.exceptions.ConfigMappingException;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internal string-to-type converter.
 */
public final class TypeConverter {
    private static final Pattern SIMPLE_DURATION = Pattern.compile("^(\\d+)(ms|s|m|h|d)$", Pattern.CASE_INSENSITIVE);

    private TypeConverter() {
    }

    public static Object convert(String value, Type genericType, Class<?> rawType, String key, ConfigValue sourceValue) {
        try {
            if (rawType == String.class) {
                return value;
            }
            if (rawType == int.class || rawType == Integer.class) {
                return Integer.parseInt(value.trim());
            }
            if (rawType == long.class || rawType == Long.class) {
                return Long.parseLong(value.trim());
            }
            if (rawType == double.class || rawType == Double.class) {
                return Double.parseDouble(value.trim());
            }
            if (rawType == boolean.class || rawType == Boolean.class) {
                return parseBoolean(value);
            }
            if (rawType == BigDecimal.class) {
                return new BigDecimal(value.trim());
            }
            if (rawType == BigInteger.class) {
                return new BigInteger(value.trim());
            }
            if (rawType == Duration.class) {
                return parseDuration(value);
            }
            if (rawType == LocalDate.class) {
                return LocalDate.parse(value.trim());
            }
            if (rawType == LocalTime.class) {
                return LocalTime.parse(value.trim());
            }
            if (rawType == LocalDateTime.class) {
                return LocalDateTime.parse(value.trim());
            }
            if (rawType == Instant.class) {
                return Instant.parse(value.trim());
            }
            if (rawType.isEnum()) {
                return parseEnum(value, rawType);
            }
            if (rawType == List.class) {
                return parseList(value, genericType, key, sourceValue);
            }
            if (rawType.isArray()) {
                return parseArray(value, rawType.getComponentType(), key, sourceValue);
            }
        } catch (ConfigMappingException e) {
            throw e;
        } catch (Exception e) {
            throw mappingError(key, rawType.getTypeName(), sourceValue, expected(rawType), e);
        }
        throw mappingError(key, rawType.getTypeName(), sourceValue, "supported scalar, enum, list, array, or record type", null);
    }

    private static Boolean parseBoolean(String value) {
        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "true", "yes", "on", "1" -> true;
            case "false", "no", "off", "0" -> false;
            default -> throw new IllegalArgumentException("Invalid boolean");
        };
    }

    private static Duration parseDuration(String value) {
        String trimmed = value.trim();
        if (trimmed.startsWith("P") || trimmed.startsWith("p")) {
            return Duration.parse(trimmed.toUpperCase(Locale.ROOT));
        }
        Matcher matcher = SIMPLE_DURATION.matcher(trimmed);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration");
        }
        long amount = Long.parseLong(matcher.group(1));
        return switch (matcher.group(2).toLowerCase(Locale.ROOT)) {
            case "ms" -> Duration.ofMillis(amount);
            case "s" -> Duration.ofSeconds(amount);
            case "m" -> Duration.ofMinutes(amount);
            case "h" -> Duration.ofHours(amount);
            case "d" -> Duration.ofDays(amount);
            default -> throw new IllegalArgumentException("Invalid duration");
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object parseEnum(String value, Class<?> rawType) {
        Class<? extends Enum> enumType = rawType.asSubclass(Enum.class);
        String normalized = value.trim().replace('-', '_');
        for (Enum<?> constant : enumType.getEnumConstants()) {
            if (constant.name().equals(value.trim()) || constant.name().equalsIgnoreCase(normalized)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("Invalid enum constant");
    }

    private static List<?> parseList(String value, Type genericType, String key, ConfigValue sourceValue) {
        Class<?> elementType = String.class;
        if (genericType instanceof ParameterizedType parameterizedType) {
            Type arg = parameterizedType.getActualTypeArguments()[0];
            if (arg instanceof Class<?> clazz) {
                elementType = clazz;
            }
        }
        if (value.trim().isEmpty()) {
            return List.of();
        }
        List<Object> result = new ArrayList<>();
        for (String part : value.split(",", -1)) {
            result.add(convert(part.trim(), elementType, elementType, key, sourceValue));
        }
        return List.copyOf(result);
    }

    private static Object parseArray(String value, Class<?> componentType, String key, ConfigValue sourceValue) {
        if (value.trim().isEmpty()) {
            return Array.newInstance(componentType, 0);
        }
        String[] parts = value.split(",", -1);
        Object array = Array.newInstance(componentType, parts.length);
        for (int i = 0; i < parts.length; i++) {
            Array.set(array, i, convert(parts[i].trim(), componentType, componentType, key, sourceValue));
        }
        return array;
    }

    private static String expected(Class<?> rawType) {
        if (rawType == int.class || rawType == Integer.class) {
            return "integer number";
        }
        if (rawType == long.class || rawType == Long.class) {
            return "long integer number";
        }
        if (rawType == double.class || rawType == Double.class) {
            return "decimal number";
        }
        if (rawType == boolean.class || rawType == Boolean.class) {
            return "true/false, yes/no, on/off, or 1/0";
        }
        if (rawType == Duration.class) {
            return "ISO-8601 duration or simple duration such as 30s, 5m, 2h, 1d, 500ms";
        }
        if (rawType.isEnum()) {
            return "one of " + List.of(rawType.getEnumConstants());
        }
        return rawType.getSimpleName();
    }

    private static ConfigMappingException mappingError(
            String key,
            String targetType,
            ConfigValue sourceValue,
            String expected,
            Exception cause
    ) {
        String value = sourceValue == null ? null : sourceValue.displayValue();
        String source = sourceValue == null ? null : sourceValue.sourceName();
        String message = "Cannot map property '" + key + "' to " + targetType + "."
                + (value == null ? "" : System.lineSeparator() + "Value: \"" + value + "\"")
                + (source == null ? "" : System.lineSeparator() + "Source: " + source)
                + System.lineSeparator() + "Expected: " + expected;
        return new ConfigMappingException(message, cause, key, source, targetType, value);
    }
}
