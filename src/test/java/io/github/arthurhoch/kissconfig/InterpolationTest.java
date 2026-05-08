package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.exceptions.ConfigInterpolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InterpolationTest {
    @TempDir
    Path tempDir;

    record Database(String url, String host, String port) {
    }

    @Test
    void interpolationUsesMergedConfigAndDefaults() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
                """);
        Files.writeString(tempDir.resolve(".env"), """
                DB_HOST=db.example
                """);

        Database config = KissConfig.builder()
                .envFile(".env")
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Database.class);

        assertEquals("jdbc:postgresql://db.example:5432/app", config.url());
    }

    @Test
    void missingInterpolationVariableThrowsClearException() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "url=${MISSING}\n");

        ConfigInterpolationException exception = assertThrows(ConfigInterpolationException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Database.class));

        assertTrue(exception.getMessage().contains("Missing interpolation variable"));
        assertTrue(exception.getMessage().contains("url"));
    }

    @Test
    void circularInterpolationThrowsClearException() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                host=${port}
                port=${host}
                url=${host}
                """);

        ConfigInterpolationException exception = assertThrows(ConfigInterpolationException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Database.class));

        assertTrue(exception.getMessage().contains("Circular interpolation reference"));
        assertTrue(exception.getMessage().contains("host"));
        assertTrue(exception.getMessage().contains("port"));
    }

    @Test
    void unclosedInterpolationThrowsClearException() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "url=${HOST\n");

        ConfigInterpolationException exception = assertThrows(ConfigInterpolationException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Database.class));

        assertTrue(exception.getMessage().contains("Unclosed interpolation expression"));
    }
}
