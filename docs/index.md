# KissConfig

[![CI](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.arthurhoch/kiss-config.svg)](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)
[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](https://github.com/arthurhoch/kiss-config/blob/main/LICENSE)
[![CodeQL](https://github.com/arthurhoch/kiss-config/actions/workflows/codeql.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/codeql.yml)
[![Docs](https://github.com/arthurhoch/kiss-config/actions/workflows/pages.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/pages.yml)

Tiny zero-dependency Java 17+ configuration library for properties, .env files, system properties, and environment variables.

Part of the KISS Java Libraries family.

> Load configuration from properties, .env files, system properties, and environment variables directly into immutable Java records.

Status: latest GitHub release is `0.1.0`; current development version is `0.1.1-SNAPSHOT`. Use a local build until Maven Central public indexing is confirmed.

## Install

Until Maven Central public indexing is confirmed, install locally:

```bash
mvn -B clean install
```

After Maven Central indexing is confirmed:

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

- [AI Usage Guide](ai-usage.md)
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
- [Security](security.md)
- [Security Hardening](security-hardening.md)
- [Secrets](secrets.md)
- [Errors](errors.md)
- [Reporting](reporting.md)
- [Examples](examples.md)
- [Testing](testing.md)
- [Testing Report](testing-report.md)
- [Safe Code Cleanup](code-cleanup.md)
- [Release](release.md)
- [Maven Central](maven-central.md)
- [GitHub Pages](github-pages.md)
- [Architecture](architecture.md)
- [Roadmap](roadmap.md)
- [AI Project Manual](AI_PROJECT_MANUAL.md)
- [Javadocs](javadocs/)

## Related KISS Projects

These libraries are independent, zero-dependency Java 17+ projects. Use only the modules you need.

| Project | Purpose |
|---|---|
| [kiss-json](https://github.com/arthurhoch/kiss-json) | Field-based JSON serialization and deserialization. |
| [kiss-requests](https://github.com/arthurhoch/kiss-requests) | Simple HTTP client built on Java HttpClient. |
| [kiss-server](https://github.com/arthurhoch/kiss-server) | Small HTTP/1.1 server for simple REST-style applications. |
| [kiss-config](https://github.com/arthurhoch/kiss-config) | Configuration loading from properties, .env files, system properties, and environment variables. |
| [kiss-binary](https://github.com/arthurhoch/kiss-binary) | Explicit binary IO for primitive binary formats. |

## Links

- [GitHub](https://github.com/arthurhoch/kiss-config)
- [Maven Central](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)
- [Javadocs](javadocs/)
- [Changelog](https://github.com/arthurhoch/kiss-config/blob/main/CHANGELOG.md)
- [Security Policy](https://github.com/arthurhoch/kiss-config/blob/main/SECURITY.md)
