package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.exceptions.ConfigInvalidProfileException;
import io.github.arthurhoch.kissconfig.exceptions.ConfigLoadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProfileAndResolutionTest {
    @TempDir
    Path tempDir;

    record Server(int port, String host, String name, String envOnly) {
    }

    record OptionalServer(Integer port) {
    }

    record HostConfig(String host) {
    }

    @Test
    void profileFilesAreResolvedForPropertiesAndEnvFiles() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                port=8080
                host=base
                """);
        Files.writeString(tempDir.resolve("application-prod.properties"), """
                port=9090
                name=prod
                """);
        Files.writeString(tempDir.resolve(".env"), """
                HOST=env-base
                ENV_ONLY=env
                """);
        Files.writeString(tempDir.resolve(".env.prod"), """
                ENV_ONLY=env-prod
                """);

        Server config = KissConfig.builder()
                .envFile(".env")
                .profile("prod")
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Server.class);

        assertEquals(8080, config.port());
        assertEquals("base", config.host());
        assertEquals("prod", config.name());
        assertEquals("env", config.envOnly());
    }

    @Test
    void overrideStrategyLetsLaterProfileAndEnvFilesOverrideEarlierFiles() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "port=8080\n");
        Files.writeString(tempDir.resolve("application-prod.properties"), "port=9090\n");

        Server config = KissConfig.builder()
                .profile("prod")
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
                .mapTo(Server.class);

        assertEquals(9090, config.port());
    }

    @Test
    void envFilesAreNotLoadedAutomatically() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "port=8080\n");
        Files.writeString(tempDir.resolve(".env"), "HOST=env\n");

        Server config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Server.class);

        assertEquals(8080, config.port());
        assertEquals(null, config.host());
    }

    @Test
    void customEnvProfileNameUsesDashBeforeExtension() throws Exception {
        Files.writeString(tempDir.resolve("myapp.env"), "HOST=base\nPORT=8080\n");
        Files.writeString(tempDir.resolve("myapp-prod.env"), "ENV_ONLY=profile\n");

        Server config = KissConfig.builder()
                .envFile("myapp.env")
                .profile("prod")
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(Server.class);

        assertEquals("base", config.host());
        assertEquals("profile", config.envOnly());
    }

    @Test
    void builderConfiguredEnvFileProfileVariantIsOptional() throws Exception {
        Path env = tempDir.resolve(".env");
        Files.writeString(env, "HOST=base\n");

        HostConfig config = KissConfig.builder()
                .envFile(env)
                .profile("prod")
                .searchOrder(SearchOrder.of(ConfigLocation.envFile()))
                .mapTo(HostConfig.class);

        assertEquals("base", config.host());
    }

    @Test
    void explicitEnvFileLocationLoadsExactPath() throws Exception {
        Path env = tempDir.resolve("secrets.env");
        Files.writeString(env, """
                HOST=explicit
                """);

        HostConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.envFile(env)))
                .mapTo(HostConfig.class);

        assertEquals("explicit", config.host());
    }

    @Test
    void invalidProfilesAreRejected() {
        assertThrows(ConfigInvalidProfileException.class, () -> KissConfig.builder().profile(""));
        assertThrows(ConfigInvalidProfileException.class, () -> KissConfig.builder().profile("../prod"));
        assertThrows(ConfigInvalidProfileException.class, () -> KissConfig.builder().profile("prod/test"));
        assertThrows(ConfigInvalidProfileException.class, () -> KissConfig.builder().profile("prod.properties"));
    }

    @Test
    void explicitMissingFilesFailUnlessIgnored() {
        Path missing = tempDir.resolve("missing.properties");

        assertThrows(ConfigLoadException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(missing)))
                .mapTo(Server.class));

        OptionalServer config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(missing)))
                .ignoreMissingExplicitFiles(true)
                .mapTo(OptionalServer.class);

        assertEquals(null, config.port());
    }

    @Test
    void duplicateResolvedFilesAreSkipped() throws Exception {
        Path file = tempDir.resolve("application.properties");
        Files.writeString(file, "port=8080\n");

        KissConfigResult<Server> result = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.file(file), ConfigLocation.file(file.toAbsolutePath().normalize())))
                .load(Server.class);

        assertEquals(8080, result.value().port());
        assertTrue(result.report().contains("Skipped duplicate resolved source"));
    }
}
