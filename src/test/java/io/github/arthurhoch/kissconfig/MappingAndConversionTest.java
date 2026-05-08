package io.github.arthurhoch.kissconfig;

import io.github.arthurhoch.kissconfig.annotations.ConfigName;
import io.github.arthurhoch.kissconfig.annotations.DefaultValue;
import io.github.arthurhoch.kissconfig.annotations.Required;
import io.github.arthurhoch.kissconfig.annotations.Secret;
import io.github.arthurhoch.kissconfig.exceptions.ConfigMappingException;
import io.github.arthurhoch.kissconfig.exceptions.ConfigMissingPropertyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappingAndConversionTest {
    @TempDir
    Path tempDir;

    enum Mode {
        DEV,
        PROD
    }

    record AppConfig(ServerConfig server) {
    }

    record ServerConfig(String host, int port) {
    }

    record NullableConfig(Integer port, String host, ServerConfig server) {
    }

    record RequiredConfig(@Required String host) {
    }

    record DefaultConfig(@DefaultValue("${SERVER_PORT:8080}") int port, @DefaultValue("PT30S") Duration timeout) {
    }

    record NamedConfig(@ConfigName("server.port") int port, @ConfigName("bind-host") String host) {
    }

    record SecretConfig(@Secret String apiKey, String password) {
    }

    record SecretNumberConfig(@Secret int pin) {
    }

    record PasswordNumberConfig(int password) {
    }

    record ConversionConfig(
            int intValue,
            long longValue,
            double doubleValue,
            boolean boolValue,
            BigDecimal decimalValue,
            BigInteger integerValue,
            Duration durationValue,
            Duration isoDuration,
            LocalDate dateValue,
            LocalTime timeValue,
            LocalDateTime dateTimeValue,
            Instant instantValue,
            Mode mode,
            List<String> names,
            List<Integer> numbers,
            List<Long> longs,
            List<Boolean> flags,
            String[] tags
    ) {
    }

    @Test
    void mapsNestedRecords() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                server.host=0.0.0.0
                server.port=8080
                """);

        AppConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(AppConfig.class);

        assertEquals("0.0.0.0", config.server().host());
        assertEquals(8080, config.server().port());
    }

    @Test
    void missingPrimitiveFailsAndMissingObjectsAreNull() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "");

        assertThrows(ConfigMissingPropertyException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(ServerConfig.class));

        NullableConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(NullableConfig.class);

        assertNull(config.port());
        assertNull(config.host());
        assertNull(config.server());
    }

    @Test
    void requiredAnnotationFailsForMissingObjects() {
        assertThrows(ConfigMissingPropertyException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.none())
                .mapTo(RequiredConfig.class));
    }

    @Test
    void defaultValueIsInterpolatedAndConverted() {
        DefaultConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.environmentVariables()))
                .environmentVariables(java.util.Map.of("SERVER_PORT", "9090"))
                .mapTo(DefaultConfig.class);

        assertEquals(9090, config.port());
        assertEquals(Duration.ofSeconds(30), config.timeout());
    }

    @Test
    void configNameSupportsFullAndLocalNames() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                server.port=8080
                bind.host=localhost
                """);

        NamedConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(NamedConfig.class);

        assertEquals(8080, config.port());
        assertEquals("localhost", config.host());
    }

    @Test
    void secretAnnotationAndAutomaticSecretKeysMaskReports() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                api.key=abc123
                password=s3cr3t
                """);

        KissConfigResult<SecretConfig> result = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .load(SecretConfig.class);

        assertEquals("abc123", result.value().apiKey());
        assertEquals("s3cr3t", result.value().password());
        assertTrue(result.report().contains("api.key = ******"));
        assertTrue(result.report().contains("password = ******"));
    }

    @Test
    void secretValuesAreMaskedInMappingExceptions() throws Exception {
        Path annotatedDir = Files.createDirectory(tempDir.resolve("annotated"));
        Files.writeString(annotatedDir.resolve("application.properties"), "pin=not-a-number\n");

        ConfigMappingException annotated = assertThrows(ConfigMappingException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(annotatedDir)))
                .mapTo(SecretNumberConfig.class));

        assertTrue(annotated.getMessage().contains("Value: \"******\""));
        assertEquals("******", annotated.safeValue().orElseThrow());
        assertTrue(!annotated.getMessage().contains("not-a-number"));

        Path automaticDir = Files.createDirectory(tempDir.resolve("automatic"));
        Files.writeString(automaticDir.resolve("application.properties"), "password=not-a-number\n");

        ConfigMappingException automatic = assertThrows(ConfigMappingException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(automaticDir)))
                .mapTo(PasswordNumberConfig.class));

        assertTrue(automatic.getMessage().contains("Value: \"******\""));
        assertEquals("******", automatic.safeValue().orElseThrow());
        assertTrue(!automatic.getMessage().contains("not-a-number"));
    }

    @Test
    void convertsSupportedTypes() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), """
                int.value=1
                long.value=2
                double.value=3.5
                bool.value=on
                decimal.value=123.45
                integer.value=123456
                duration.value=500ms
                iso.duration=PT30S
                date.value=2026-05-06
                time.value=10:15:30
                date.time.value=2026-05-06T10:15:30
                instant.value=2026-05-06T10:15:30Z
                mode=prod
                names=alice,bob
                numbers=1,2,3
                longs=4,5
                flags=true,no,on
                tags=a,b
                """);

        ConversionConfig config = KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(ConversionConfig.class);

        assertEquals(1, config.intValue());
        assertEquals(2L, config.longValue());
        assertEquals(3.5, config.doubleValue());
        assertEquals(true, config.boolValue());
        assertEquals(new BigDecimal("123.45"), config.decimalValue());
        assertEquals(new BigInteger("123456"), config.integerValue());
        assertEquals(Duration.ofMillis(500), config.durationValue());
        assertEquals(Duration.ofSeconds(30), config.isoDuration());
        assertEquals(LocalDate.parse("2026-05-06"), config.dateValue());
        assertEquals(LocalTime.parse("10:15:30"), config.timeValue());
        assertEquals(LocalDateTime.parse("2026-05-06T10:15:30"), config.dateTimeValue());
        assertEquals(Instant.parse("2026-05-06T10:15:30Z"), config.instantValue());
        assertEquals(Mode.PROD, config.mode());
        assertEquals(List.of("alice", "bob"), config.names());
        assertEquals(List.of(1, 2, 3), config.numbers());
        assertEquals(List.of(4L, 5L), config.longs());
        assertEquals(List.of(true, false, true), config.flags());
        assertArrayEquals(new String[]{"a", "b"}, config.tags());
    }

    @Test
    void conversionErrorsAreActionable() throws Exception {
        Files.writeString(tempDir.resolve("application.properties"), "port=abc\nhost=localhost\n");

        ConfigMappingException exception = assertThrows(ConfigMappingException.class, () -> KissConfig.builder()
                .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
                .mapTo(ServerConfig.class));

        assertTrue(exception.getMessage().contains("Cannot map property 'port'"));
        assertTrue(exception.getMessage().contains("Expected: integer number"));
        assertTrue(exception.getMessage().contains("Value: \"abc\""));
    }
}
