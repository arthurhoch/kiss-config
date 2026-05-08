package io.github.arthurhoch.kissconfig.internal.parse;

import io.github.arthurhoch.kissconfig.exceptions.ConfigParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Internal .env parser.
 */
public final class EnvConfigParser {
    private EnvConfigParser() {
    }

    public static List<ParsedConfigEntry> parse(InputStream inputStream, String sourceName) {
        List<ParsedConfigEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                parseLine(line, lineNumber, sourceName, entries);
            }
        } catch (ConfigParseException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigParseException("Cannot read env source '" + sourceName + "': " + e.getMessage(), e, sourceName);
        }
        return entries;
    }

    private static void parseLine(String line, int lineNumber, String sourceName, List<ParsedConfigEntry> entries) {
        String trimmed = line.trim();
        if (trimmed.isEmpty() || trimmed.startsWith("#")) {
            return;
        }
        if (trimmed.startsWith("export ")) {
            trimmed = trimmed.substring("export ".length()).trim();
        }

        int equals = trimmed.indexOf('=');
        if (equals <= 0) {
            throw parseError(sourceName, lineNumber, "Expected KEY=value");
        }

        String key = trimmed.substring(0, equals).trim();
        if (key.isEmpty()) {
            throw parseError(sourceName, lineNumber, "Missing key before '='");
        }
        String rawValue = trimmed.substring(equals + 1).stripLeading();
        entries.add(new ParsedConfigEntry(key, parseValue(rawValue, sourceName, lineNumber), lineNumber));
    }

    private static String parseValue(String value, String sourceName, int lineNumber) {
        if (value.isEmpty()) {
            return "";
        }
        if (value.charAt(0) == '"') {
            return parseDoubleQuoted(value, sourceName, lineNumber);
        }
        if (value.charAt(0) == '\'') {
            return parseSingleQuoted(value, sourceName, lineNumber);
        }
        return value.trim();
    }

    private static String parseSingleQuoted(String value, String sourceName, int lineNumber) {
        int end = value.indexOf('\'', 1);
        if (end < 0) {
            throw parseError(sourceName, lineNumber, "Unterminated single-quoted value");
        }
        requireTrailingWhitespace(value.substring(end + 1), sourceName, lineNumber);
        return value.substring(1, end);
    }

    private static String parseDoubleQuoted(String value, String sourceName, int lineNumber) {
        StringBuilder result = new StringBuilder();
        boolean escaping = false;
        for (int index = 1; index < value.length(); index++) {
            char ch = value.charAt(index);
            if (escaping) {
                result.append(switch (ch) {
                    case 'n' -> '\n';
                    case 't' -> '\t';
                    case 'r' -> '\r';
                    case '"' -> '"';
                    case '\\' -> '\\';
                    case '=' -> '=';
                    default -> ch;
                });
                escaping = false;
                continue;
            }
            if (ch == '\\') {
                escaping = true;
                continue;
            }
            if (ch == '"') {
                requireTrailingWhitespace(value.substring(index + 1), sourceName, lineNumber);
                return result.toString();
            }
            result.append(ch);
        }
        throw parseError(sourceName, lineNumber, "Unterminated double-quoted value");
    }

    private static void requireTrailingWhitespace(String trailing, String sourceName, int lineNumber) {
        if (!trailing.trim().isEmpty()) {
            throw parseError(sourceName, lineNumber, "Unexpected characters after quoted value");
        }
    }

    private static ConfigParseException parseError(String sourceName, int lineNumber, String detail) {
        return new ConfigParseException(
                "Cannot parse env source '" + sourceName + "' at line " + lineNumber + ": " + detail,
                null,
                sourceName
        );
    }
}
