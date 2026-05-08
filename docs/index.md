# KissConfig

[![CI](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.arthurhoch/kiss-config.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)

KissConfig is a zero-dependency Java 17+ configuration library.

> Load configuration from properties, .env files, system properties, and environment variables directly into immutable Java records.

## Install

KissConfig is Maven Central-ready. Before the first published release, install locally with:

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

## Quick Start

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

## Core Concepts

- `SearchOrder` is read order.
- `MergeStrategy.FILL_MISSING_ONLY` is the default.
- `.env` files are explicit and are not loaded by default.
- Environment variables normalize to dot-form keys.
- Java records are the primary mapping target.
- Reports show loaded and skipped sources with secrets masked.

## Reference

- [API](api.md)
- [Search Order](search-order.md)
- [Config Locations](config-locations.md)
- [Merge Strategy](merge-strategy.md)
- [Profiles](profiles.md)
- [Interpolation](interpolation.md)
- [Mapping](mapping.md)
- [Annotations](annotations.md)
- [Reporting](reporting.md)
- [Javadocs](javadocs/)
