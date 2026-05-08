package io.github.arthurhoch.kissconfig.internal.source;

import io.github.arthurhoch.kissconfig.ConfigLocationType;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

record SourceCandidate(
        String name,
        ConfigLocationType type,
        Path path,
        URL url,
        Map<String, String> map,
        SourceFormat format,
        boolean explicit,
        boolean optional,
        String skipMessage
) {
    static SourceCandidate file(
            String name,
            ConfigLocationType type,
            Path path,
            SourceFormat format,
            boolean explicit,
            boolean optional
    ) {
        return new SourceCandidate(name, type, path, null, null, format, explicit, optional, null);
    }

    static SourceCandidate url(String name, ConfigLocationType type, URL url, SourceFormat format) {
        return new SourceCandidate(name, type, null, url, null, format, false, true, null);
    }

    static SourceCandidate missingClasspath(String name, ConfigLocationType type) {
        return new SourceCandidate(name, type, null, null, null, SourceFormat.PROPERTIES, false, true, null);
    }

    static SourceCandidate map(String name, ConfigLocationType type, Map<String, String> map) {
        return new SourceCandidate(name, type, null, null, map, SourceFormat.MAP, false, true, null);
    }

    static SourceCandidate skipped(String name, ConfigLocationType type, String message) {
        return new SourceCandidate(name, type, null, null, null, null, false, true, message);
    }

    boolean preSkipped() {
        return skipMessage != null;
    }
}
