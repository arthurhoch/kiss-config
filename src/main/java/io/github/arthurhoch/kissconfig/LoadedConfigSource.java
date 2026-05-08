package io.github.arthurhoch.kissconfig;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

/**
 * Describes an attempted configuration source and whether it was loaded or skipped.
 *
 * @param name source name
 * @param type source type
 * @param path source path when available
 * @param loaded whether the source was loaded
 * @param optional whether the source was optional
 * @param explicit whether the source was explicitly configured by the user
 * @param valueCount number of values loaded from the source
 * @param message diagnostic message
 */
public record LoadedConfigSource(
        String name,
        ConfigLocationType type,
        Path path,
        boolean loaded,
        boolean optional,
        boolean explicit,
        int valueCount,
        String message
) {
    /**
     * Creates source metadata.
     */
    public LoadedConfigSource {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(message, "message");
    }

    /**
     * Returns the optional source path.
     *
     * @return optional path
     */
    public Optional<Path> optionalPath() {
        return Optional.ofNullable(path);
    }
}
