# KissConfig

[![CI](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.arthurhoch/kiss-config.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)

KissConfig is a zero-dependency Java configuration library.

> Load configuration from properties, .env files, system properties, and environment variables directly into immutable Java records.

## Install

KissConfig is prepared for Maven Central publishing. Until the first release is published, use a local build:

```bash
mvn -B clean install
```

After release:

```xml
<dependency>
    <groupId>io.github.arthurhoch</groupId>
    <artifactId>kiss-config</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Simple

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}

AppConfig config = KissConfig.load(AppConfig.class);
```

`application.properties`:

```properties
server.host=0.0.0.0
server.port=8080
```

## Production

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mapTo(AppConfig.class);
```

## Override Behavior

The default merge strategy is `MergeStrategy.FILL_MISSING_ONLY`. Earlier sources fill values first and later sources only add missing keys.

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

With `OVERRIDE_EXISTING`, later sources override earlier sources.

## Custom Order

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .searchOrder(SearchOrder.of(
                ConfigLocation.classpath(),
                ConfigLocation.jarDirectory(),
                ConfigLocation.workingDirectory(),
                ConfigLocation.path("/etc/myapp"),
                ConfigLocation.envFile("/run/secrets/myapp.env"),
                ConfigLocation.systemProperties(),
                ConfigLocation.environmentVariables()
        ))
        .mapTo(AppConfig.class);
```

## Result Report

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

Reports include attempted sources, loaded sources, skipped optional sources, effective values, source metadata, merge strategy, profile, configured file names, and masked secrets.

## Core Concepts

- `SearchOrder` is the order of reading, not a priority list by name.
- `MergeStrategy.FILL_MISSING_ONLY` is the default.
- `.env` files are not loaded unless configured with `.envFile(...)` or an explicit `ConfigLocation.envFile(...)`.
- Environment variables such as `SERVER_PORT` normalize to `server.port`.
- Library defaults are loaded only from `META-INF/kiss-config/defaults.properties`.
- Java records are the primary mapping target.

## Documentation

- [Getting Started](docs/getting-started.md)
- [Search Order](docs/search-order.md)
- [Config Locations](docs/config-locations.md)
- [Merge Strategy](docs/merge-strategy.md)
- [Mapping](docs/mapping.md)
- [Release](docs/release.md)
- [Architecture](docs/architecture.md)

Generated Javadocs are published by the GitHub Pages workflow under `/javadocs/`.

## License

Apache License 2.0.
