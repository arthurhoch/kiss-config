package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.exceptions.ConfigDuplicateKeyException;
import io.github.arthurhoch.kissconfig.internal.normalize.KeyNormalizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParsingNormalizationAndMergeTest {
    @TempDir
    Path tempDir;

    record Server(int port, String host, String text, String escaped) {
    }

    @Test
    void keyNormalizationUsesCanonicalDotForm() {
        assertEquals("server.port", KeyNormalizer.normalize("SERVER_PORT"));
        assertEquals("server.port", KeyNormalizer.normalize("server.port"));
        assertEquals("server.port", KeyNormalizer.normalize("server_port"));
        assertEquals("server.port", KeyNormalizer.normalize("server-port"));
        assertEquals("database.pool.size", KeyNormalizer.normalize("database.pool-size"));
        assertEquals("database.pool.size", KeyNormalizer.normalize("DATABASE_POOL_SIZE"));
        assertEquals("v1.port", KeyNormalizer.normalize("V1_PORT"));
    }

    @Test
    void propertiesParserSupportsStandardSyntaxThroughLoader() throws Exception {
        Path file = tempDir.resolve("application.properties");
        Files.writeString(file, """
                # comment
                port=8080
                host:localhost
                text hello
                """);

        Server config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Server.class);

        assertEquals(8080, config.port());
        assertEquals("localhost", config.host());
        assertEquals("hello", config.text());
    }

    @Test
    void envParserSupportsCommentsQuotesEscapesAndExport() throws Exception {
        Path file = tempDir.resolve(".env");
        Files.writeString(file, """
                # comment
                export PORT=8080
                HOST='localhost'
                TEXT="hello\\nworld"
                ESCAPED="a\\=b"
                """);

        Server config = KissConfig.builder()
                .envFile(".env")
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Server.class);

        assertEquals(8080, config.port());
        assertEquals("localhost", config.host());
        assertEquals("hello\nworld", config.text());
        assertEquals("a=b", config.escaped());
    }

    @Test
    void fillMissingOnlyKeepsFirstValue() throws Exception {
        Path first = tempDir.resolve("first.properties");
        Path second = tempDir.resolve("second.properties");
        Files.writeString(first, "port=8080\nhost=first\n");
        Files.writeString(second, "port=9090\ntext=second\n");

        Server config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(first), ConfigLocation.file(second)))
                .mapTo(Server.class);

        assertEquals(8080, config.port());
        assertEquals("first", config.host());
        assertEquals("second", config.text());
    }

    @Test
    void overrideExistingKeepsLaterValue() throws Exception {
        Path first = tempDir.resolve("first.properties");
        Path second = tempDir.resolve("second.properties");
        Files.writeString(first, "port=8080\n");
        Files.writeString(second, "port=9090\n");

        Server config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(first), ConfigLocation.file(second)))
                .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
                .mapTo(Server.class);

        assertEquals(9090, config.port());
    }

    @Test
    void failOnDuplicateRejectsRepeatedCanonicalKeysAcrossSources() throws Exception {
        Path first = tempDir.resolve("first.properties");
        Path second = tempDir.resolve("second.properties");
        Files.writeString(first, "SERVER_PORT=8080\n");
        Files.writeString(second, "server.port=9090\n");

        assertThrows(ConfigDuplicateKeyException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(first), ConfigLocation.file(second)))
                .mergeStrategy(MergeStrategy.FAIL_ON_DUPLICATE)
                .mapTo(Server.class));
    }

    @Test
    void systemPropertiesAndEnvironmentVariablesCanBeInjectedForTests() {
        record PortConfig(int serverPort, String databaseUrl) {
        }

        PortConfig fromSystem = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.systemProperties()))
                .systemProperties(Map.of("SERVER_PORT", "8080", "DATABASE_URL", "jdbc:test"))
                .mapTo(PortConfig.class);

        PortConfig fromEnv = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.environmentVariables()))
                .environmentVariables(Map.of("SERVER_PORT", "9090", "DATABASE_URL", "jdbc:env"))
                .mapTo(PortConfig.class);

        assertEquals(8080, fromSystem.serverPort());
        assertEquals("jdbc:test", fromSystem.databaseUrl());
        assertEquals(9090, fromEnv.serverPort());
        assertEquals("jdbc:env", fromEnv.databaseUrl());
    }

    @Test
    void reportIncludesLoadedAndEffectiveValues() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "port=8080\npassword=secret\n");

        KissConfigResult<Server> result = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .load(Server.class);

        assertTrue(result.report().contains("LOADED"));
        assertTrue(result.report().contains("port = 8080"));
        assertTrue(result.report().contains("password = ******"));
    }
}
