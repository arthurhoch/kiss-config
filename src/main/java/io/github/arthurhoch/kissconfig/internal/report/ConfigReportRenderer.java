package io.github.arthurhoch.kissconfig.internal.report;

import io.github.arthurhoch.kissconfig.ConfigMap;
import io.github.arthurhoch.kissconfig.ConfigValue;
import io.github.arthurhoch.kissconfig.LoadedConfigSource;
import io.github.arthurhoch.kissconfig.internal.source.LoadOptions;

import java.util.Comparator;

/**
 * Internal text report renderer.
 */
public final class ConfigReportRenderer {
    private ConfigReportRenderer() {
    }

    public static String render(LoadOptions options, ConfigMap configMap, java.util.List<LoadedConfigSource> sources) {
        StringBuilder report = new StringBuilder();
        report.append("KissConfig report").append(System.lineSeparator());
        report.append("Property file: ").append(options.propertyFile()).append(System.lineSeparator());
        report.append("Env file: ").append(options.envFile() == null ? "(none)" : options.envFile()).append(System.lineSeparator());
        report.append("Profile: ").append(options.profile() == null ? "(none)" : options.profile()).append(System.lineSeparator());
        report.append("Merge strategy: ").append(options.mergeStrategy()).append(System.lineSeparator());
        report.append("Search order:").append(System.lineSeparator());
        options.searchOrder().locations().forEach(location -> report.append("  - ").append(location).append(System.lineSeparator()));

        report.append("Sources:").append(System.lineSeparator());
        for (LoadedConfigSource source : sources) {
            report.append("  - ")
                    .append(source.loaded() ? "LOADED " : "SKIPPED ")
                    .append(source.name())
                    .append(" [").append(source.type()).append("]")
                    .append(source.loaded() ? " values=" + source.valueCount() : " reason=" + source.message())
                    .append(System.lineSeparator());
        }

        report.append("Effective values:").append(System.lineSeparator());
        configMap.valuesByKey().values().stream()
                .sorted(Comparator.comparing(ConfigValue::canonicalKey))
                .forEach(value -> report.append("  - ")
                        .append(value.canonicalKey())
                        .append(" = ")
                        .append(value.displayValue())
                        .append(" (source: ")
                        .append(value.sourceName())
                        .append(")")
                        .append(System.lineSeparator()));
        return report.toString();
    }
}
