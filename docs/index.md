# KissConfig

[![CI](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/badge/Maven%20Central-after%20release-lightgrey.svg)](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)

KissConfig is a zero-production-dependency Java 17+ configuration library.

> Load configuration from properties, .env files, system properties, and environment variables directly into immutable Java records.

## Install

Before the first Maven Central release is published, install locally:

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
import io.github.arthurhoch.kissconfig.KissConfig;

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
- `OVERRIDE_EXISTING` and `FAIL_ON_DUPLICATE` are opt-in.
- `.env` files are explicit and are not loaded by default.
- Environment variables normalize to dot-form keys.
- Java records are the supported mapping target in v0.1.0.
- Reports show loaded and skipped sources with secrets masked.

## Reference

- [Getting Started](getting-started.md)
- [API](api.md)
- [Search Order](search-order.md)
- [Config Locations](config-locations.md)
- [Merge Strategy](merge-strategy.md)
- [Profiles](profiles.md)
- [Env Files](env-files.md)
- [Properties Files](properties-files.md)
- [Interpolation](interpolation.md)
- [Mapping](mapping.md)
- [Annotations](annotations.md)
- [Secrets](secrets.md)
- [Errors](errors.md)
- [Reporting](reporting.md)
- [Examples](examples.md)
- [Testing](testing.md)
- [Release](release.md)
- [Maven Central](maven-central.md)
- [GitHub Pages](github-pages.md)
- [Architecture](architecture.md)
- [Roadmap](roadmap.md)
- [AI Project Manual](AI_PROJECT_MANUAL.md)
- [Javadocs](javadocs/)
