package io.github.arthurhoch.kissconfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchOrderAndLocationTest {
    @TempDir
    Path tempDir;

    record LibraryDefaults(int port, String host) {
    }

    @Test
    void defaultsUseDocumentedOrder() {
        List<ConfigLocationType> types = SearchOrder.defaults().locations().stream()
                .map(ConfigLocation::type)
                .toList();

        assertEquals(List.of(
                ConfigLocationType.CLASSPATH_LIBRARIES,
                ConfigLocationType.CLASSPATH,
                ConfigLocationType.JAR_DIRECTORY,
                ConfigLocationType.WORKING_DIRECTORY,
                ConfigLocationType.SYSTEM_PROPERTIES,
                ConfigLocationType.ENVIRONMENT_VARIABLES
        ), types);
    }

    @Test
    void externalFirstAndProductionUseExternalPriorityOrderForDefaultMerge() {
        List<ConfigLocationType> expected = List.of(
                ConfigLocationType.ENVIRONMENT_VARIABLES,
                ConfigLocationType.SYSTEM_PROPERTIES,
                ConfigLocationType.ENV_FILE,
                ConfigLocationType.WORKING_DIRECTORY,
                ConfigLocationType.JAR_DIRECTORY,
                ConfigLocationType.CLASSPATH,
                ConfigLocationType.CLASSPATH_LIBRARIES
        );

        assertEquals(expected, SearchOrder.externalFirst().locations().stream().map(ConfigLocation::type).toList());
        assertEquals(expected, SearchOrder.production().locations().stream().map(ConfigLocation::type).toList());
    }

    @Test
    void otherPresetsUseDocumentedOrder() {
        assertEquals(List.of(ConfigLocationType.CLASSPATH_LIBRARIES, ConfigLocationType.CLASSPATH),
                SearchOrder.classpathOnly().locations().stream().map(ConfigLocation::type).toList());
        assertEquals(List.of(ConfigLocationType.WORKING_DIRECTORY, ConfigLocationType.CLASSPATH),
                SearchOrder.test().locations().stream().map(ConfigLocation::type).toList());
        assertTrue(SearchOrder.none().locations().isEmpty());
    }

    @Test
    void factoriesCaptureTypesAndPaths() {
        Path directory = Path.of("/tmp/kiss-config");
        Path file = Path.of("/tmp/kiss-config/application.properties");

        assertEquals(ConfigLocationType.CLASSPATH, ConfigLocation.classpath().type());
        assertEquals(ConfigLocationType.CLASSPATH_LIBRARIES, ConfigLocation.classpathLibraries().type());
        assertEquals(ConfigLocationType.JAR_DIRECTORY, ConfigLocation.jarDirectory().type());
        assertEquals(ConfigLocationType.WORKING_DIRECTORY, ConfigLocation.workingDirectory().type());
        assertEquals(directory, ConfigLocation.path(directory).path().orElseThrow());
        assertEquals(file, ConfigLocation.file(file).path().orElseThrow());
        assertEquals(ConfigLocationType.ENV_FILE, ConfigLocation.envFile().type());
        assertEquals(file, ConfigLocation.envFile(file).path().orElseThrow());
        assertEquals(ConfigLocationType.SYSTEM_PROPERTIES, ConfigLocation.systemProperties().type());
        assertEquals(ConfigLocationType.ENVIRONMENT_VARIABLES, ConfigLocation.environmentVariables().type());
    }

    @Test
    void classpathLibrariesLoadOnlyKissConfigDefaultsResource() throws Exception {
        Path defaults = tempDir.resolve("META-INF/kiss-config/defaults.properties");
        Files.createDirectories(defaults.getParent());
        Files.writeString(defaults, """
                port=8080
                host=library
                """);
        Files.writeString(tempDir.resolve("application.properties"), "port=9090\nhost=ignored\n");

        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{tempDir.toUri().toURL()}, null)) {
            LibraryDefaults config = KissConfig.builder()
                    .searchOrder(SearchOrder.of(ConfigLocation.classpathLibraries()))
                    .classLoader(classLoader)
                    .mapTo(LibraryDefaults.class);

            assertEquals(8080, config.port());
            assertEquals("library", config.host());
        }
    }
}
