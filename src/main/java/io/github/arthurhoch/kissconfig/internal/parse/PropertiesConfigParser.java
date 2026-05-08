package io.github.arthurhoch.kissconfig.internal.parse;

import io.github.arthurhoch.kissconfig.exceptions.ConfigParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Internal .properties parser backed by java.util.Properties.
 */
public final class PropertiesConfigParser {
    private PropertiesConfigParser() {
    }

    public static List<ParsedConfigEntry> parse(InputStream inputStream, String sourceName) {
        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (Exception e) {
            throw new ConfigParseException("Cannot parse properties source '" + sourceName + "': " + e.getMessage(), e, sourceName);
        }

        List<String> names = new ArrayList<>(properties.stringPropertyNames());
        names.sort(String::compareTo);

        List<ParsedConfigEntry> entries = new ArrayList<>();
        for (String name : names) {
            entries.add(new ParsedConfigEntry(name, properties.getProperty(name), null));
        }
        return entries;
    }
}
