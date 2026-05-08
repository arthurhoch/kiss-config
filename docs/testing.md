---
layout: default
---

# Testing

KissConfig's project test suite uses JUnit Jupiter. Production code has zero dependencies.

## Application Tests

Use a temporary directory and a focused record:

```java
@TempDir
Path tempDir;

record ServerConfig(String host, int port) {}

@Test
void loadsServerConfig() throws Exception {
    Files.writeString(tempDir.resolve("application.properties"), """
            host=0.0.0.0
            port=8080
            """);

    ServerConfig config = KissConfig.builder()
            .searchOrder(SearchOrder.of(ConfigLocation.path(tempDir)))
            .mapTo(ServerConfig.class);

    assertEquals(8080, config.port());
}
```

## Deterministic Source Order

Prefer explicit search orders in tests:

```java
SearchOrder.of(ConfigLocation.path(tempDir))
```

Avoid tests that depend on the real machine environment. KissConfig internals abstract system properties and environment variables so the project test suite can provide deterministic maps.

## What The Project Tests Must Cover

- search order presets
- config location factories
- profile filename expansion
- `.env` opt-in behavior
- explicit env files
- key normalization
- `.properties` parsing
- `.env` parsing
- system properties and environment variables through deterministic maps
- all merge strategies
- interpolation defaults, missing variables, and cycles
- record mapping and nested records
- primitive missing failures and nullable object behavior
- annotations
- type conversion
- secret masking in reports and exceptions
- report generation
- duplicate file deduplication
- invalid profile rejection
- zero compile-scope production dependencies

Do not weaken tests to make a build pass.
