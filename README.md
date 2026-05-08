# KissConfig

[![CI](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.arthurhoch/kiss-config.svg)](https://central.sonatype.com/artifact/io.github.arthurhoch/kiss-config)
[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue.svg)](LICENSE)
[![CodeQL](https://github.com/arthurhoch/kiss-config/actions/workflows/codeql.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/codeql.yml)
[![Docs](https://github.com/arthurhoch/kiss-config/actions/workflows/pages.yml/badge.svg)](https://github.com/arthurhoch/kiss-config/actions/workflows/pages.yml)

Tiny zero-dependency Java 17+ configuration library for properties, .env files, system properties, and environment variables.

Part of the KISS Java Libraries family: small, explicit, zero-dependency Java 17+ libraries. Each project is independent. Use only the modules you need.

> Load configuration from properties, .env files, system properties, and environment variables directly into immutable Java records.

KissConfig reads configuration sources, normalizes keys, merges them deterministically, interpolates variables, and maps the result into a typed config object. Java records are the supported mapping target in v0.1.0.

## Status

Latest GitHub release: `0.1.0`.

The release workflow completed for `0.1.0`, but the artifact was not yet visible from Maven Central public repositories during this audit. Until Maven Central indexing is confirmed, use a local build.

## Why this exists

KissConfig exists for Java projects that need deterministic configuration loading without Spring, MicroProfile Config, cloud secret managers, YAML/TOML/JSON parsers, or runtime dependencies. It keeps source order, merge behavior, interpolation, mapping, and secret masking explicit.

## Install

Use a local build until Maven Central availability is confirmed:

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

By default KissConfig searches for `application.properties`, uses `SearchOrder.defaults()`, merges with `MergeStrategy.FILL_MISSING_ONLY`, interpolates values, and maps to the requested record.

## Records Are The Supported Target

Records are the intended v0.1.0 style:

```java
public record AppConfig(ServerConfig server, DatabaseConfig database) {}
public record ServerConfig(String host, int port) {}
public record DatabaseConfig(String url) {}
```

```properties
server.host=0.0.0.0
server.port=8080
database.url=jdbc:postgresql://localhost:5432/app
```

Normal JavaBeans, field-based classes, setters, and arbitrary constructors are not supported in v0.1.0. Passing a normal class currently fails with `ConfigMappingException`. Support for simple JavaBeans or field-based classes is a roadmap item.

## Design Principles

- KISS: keep configuration loading deterministic, explicit, and inspectable.
- Zero production dependencies.
- Java 17+ standard APIs.
- Small public API centered on records, builder configuration, source order, merge strategy, and reports.
- Predictable errors and secret masking in reports and exceptions.
- No framework lock-in.

## Explicit Builder

```java
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.MergeStrategy;
import io.github.arthurhoch.kissconfig.SearchOrder;

AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mergeStrategy(MergeStrategy.FILL_MISSING_ONLY)
        .mapTo(AppConfig.class);
```

Use `load(AppConfig.class)` instead of `mapTo(AppConfig.class)` when you need diagnostics:

```java
import io.github.arthurhoch.kissconfig.KissConfigResult;

KissConfigResult<AppConfig> result = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

## Production Profile Example

```java
import io.github.arthurhoch.kissconfig.ConfigLocation;
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.SearchOrder;

AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")))
        .mapTo(AppConfig.class);
```

KissConfig attempts these files in this read order:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

With the default `FILL_MISSING_ONLY`, `/opt/app/application.properties` fills first, the profile file only fills missing keys, `.env` only fills keys still missing, and `.env.prod` only fills keys still missing.

If prod or env files should override base files, opt in:

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")))
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

## Env Files

`.env` files are opt-in. KissConfig never loads `.env` unless `.envFile(...)` is configured on the builder or `ConfigLocation.envFile(...)` is explicitly present in the search order.

```java
AppConfig config = KissConfig.builder()
        .envFile(".env")
        .mapTo(AppConfig.class);
```

Supported `.env` style:

```dotenv
# comment
VAR1=teste
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/app
export FEATURE_ENABLED=true
QUOTED="hello\nworld"
SINGLE='literal value'
```

Double-quoted values support `\n`, `\t`, `\r`, `\"`, `\\`, and `\=`. KissConfig does not execute shell commands and does not implement complex shell interpolation.

Explicit env path:

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env")))
        .mapTo(AppConfig.class);
```

An explicit `ConfigLocation.envFile(path)` loads that exact file. It does not add a profile variant automatically. If a separate profile-specific explicit env file is needed, list it explicitly.

Builder `.envFile(path)` can also receive an absolute path. Directory locations resolve an absolute env file path as that absolute path, and profile expansion still applies:

```java
AppConfig config = KissConfig.builder()
        .envFile("/run/secrets/myapp.env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.workingDirectory()))
        .mapTo(AppConfig.class);
```

This attempts:

```text
/run/secrets/myapp.env
/run/secrets/myapp-prod.env
```

## System Properties And Environment Variables

Java system properties:

```bash
java -Dserver.port=9090 -jar app.jar
```

Operating-system environment variables:

```bash
SERVER_PORT=9090 java -jar app.jar
```

`SERVER_PORT`, `server_port`, `server-port`, and `server.port` all normalize to the canonical key `server.port`.

With `SearchOrder.defaults()` and the default `FILL_MISSING_ONLY`, classpath and file values are read before system properties and environment variables. If external values should win with the default merge strategy, use `SearchOrder.externalFirst()` or `SearchOrder.production()`.

## Custom SearchOrder

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

`SearchOrder` is read order, not priority by name. Conflict priority is controlled by `MergeStrategy`.

| Merge Strategy | Source order A -> B | Effective value when both define same key |
|---|---|---|
| `FILL_MISSING_ONLY` | A then B | A |
| `OVERRIDE_EXISTING` | A then B | B |
| `FAIL_ON_DUPLICATE` | A then B | error |

`SearchOrder.externalFirst()` reads external values before classpath values. This is designed for the default `FILL_MISSING_ONLY`, where earlier sources win. With `OVERRIDE_EXISTING`, later sources win, so the meaning changes.

## Merge Strategies

Default:

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FILL_MISSING_ONLY)
        .mapTo(AppConfig.class);
```

Opt-in override:

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

Opt-in duplicate auditing:

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FAIL_ON_DUPLICATE)
        .mapTo(AppConfig.class);
```

## Profiles

One profile is supported in v0.1.0:

```java
KissConfig.builder().profile("prod");
```

Profile names must match `[a-zA-Z0-9_-]+`. Profiles are never enabled automatically; `SearchOrder.production()` does not imply `profile("prod")`.

Filename expansion:

| Base file | Profile file |
|---|---|
| `application.properties` | `application-prod.properties` |
| `.env` | `.env.prod` |
| `myapp.properties` | `myapp-prod.properties` |
| `myapp.env` | `myapp-prod.env` |

## Annotations

```java
import io.github.arthurhoch.kissconfig.annotations.ConfigName;
import io.github.arthurhoch.kissconfig.annotations.DefaultValue;
import io.github.arthurhoch.kissconfig.annotations.Required;
import io.github.arthurhoch.kissconfig.annotations.Secret;

public record ServerConfig(
        @ConfigName("server.host") String host,
        @DefaultValue("8080") int port,
        @Required String name,
        @Secret String token
) {}
```

- `@ConfigName("server.host")`: dotted values are full keys.
- `@ConfigName("bind-host")`: values without dots are local component names.
- `@DefaultValue`: used only when no value exists; defaults are interpolated and converted.
- `@Required`: missing nullable values fail.
- `@Secret`: masks the value in reports and exceptions.

## Interpolation

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
server.port=${SERVER_PORT:8080}
```

Supported forms are `${NAME}` and `${NAME:default}`. Interpolation happens after all sources are merged, so values can reference variables from earlier or later sources. Missing required variables and circular references throw `ConfigInterpolationException`.

## Report And Secret Masking

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .envFile(".env")
        .load(AppConfig.class);

System.out.println(result.report());
```

Reports include attempted sources, loaded sources, skipped missing optional sources, effective values, source metadata, merge strategy, profile, configured file names, and search order.

Secrets are masked when a record component has `@Secret` or when the canonical key looks secret, including `password`, `passwd`, `secret`, `token`, `credential`, `api.key`, and `private.key`.

## Supported Types

- `String`
- primitive and wrapper `int`, `long`, `double`, `boolean`
- `BigDecimal`
- `BigInteger`
- `Duration`
- `LocalDate`
- `LocalTime`
- `LocalDateTime`
- `Instant`
- enums
- `List<String>`, `List<Integer>`, `List<Long>`, `List<Boolean>`
- arrays of supported scalar types

Booleans accept `true/false`, `yes/no`, `on/off`, and `1/0`. Durations accept ISO-8601 values such as `PT30S` and simple values such as `30s`, `5m`, `2h`, `1d`, and `500ms`. Lists are comma-separated and trimmed; escaped commas are not supported in v0.1.0.

## Limitations In v0.1.0

- Java records only for mapping targets.
- One profile only.
- No hot reload.
- No YAML, TOML, or JSON in core.
- No Bean Validation dependency.
- No framework adapters.
- No cloud secret manager integration.
- No logging framework dependency.
- No conversion SPI.
- No escaped commas in lists.

## Related KISS Projects

These libraries are independent, zero-dependency Java 17+ projects. Use only the modules you need.

| Project | Purpose |
|---|---|
| [kiss-json](https://github.com/arthurhoch/kiss-json) | Field-based JSON serialization and deserialization. |
| [kiss-requests](https://github.com/arthurhoch/kiss-requests) | Simple HTTP client built on Java HttpClient. |
| [kiss-server](https://github.com/arthurhoch/kiss-server) | Small HTTP/1.1 server for simple REST-style applications. |
| [kiss-config](https://github.com/arthurhoch/kiss-config) | Configuration loading from properties, .env files, system properties, and environment variables. |
| [kiss-binary](https://github.com/arthurhoch/kiss-binary) | Explicit binary IO for primitive binary formats. |

## Documentation

- [GitHub Pages](https://arthurhoch.github.io/kiss-config/)
- [Getting Started](docs/getting-started.md)
- [AI Usage Guide](docs/ai-usage.md)
- [API](docs/api.md)
- [Search Order](docs/search-order.md)
- [Config Locations](docs/config-locations.md)
- [Merge Strategy](docs/merge-strategy.md)
- [Profiles](docs/profiles.md)
- [Env Files](docs/env-files.md)
- [Mapping](docs/mapping.md)
- [Examples](docs/examples.md)
- [Testing](docs/testing.md)
- [Testing Report](docs/testing-report.md)
- [Safe Code Cleanup](docs/code-cleanup.md)
- [Security Hardening](docs/security-hardening.md)
- [Release](docs/release.md)
- [AI Project Manual](docs/AI_PROJECT_MANUAL.md)

Generated Javadocs are published by the GitHub Pages workflow under `/javadocs/`.

## Requirements

- Java 17 or newer.
- Maven for building from source.

## Build

```bash
mvn -B clean verify
mvn -B test jacoco:report
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

The compile-scope dependency list must remain `none`.

Additional configured profiles:

```bash
mvn -Pspotbugs verify
mvn -Pdependency-check verify
```

## Security and Quality

GitHub Actions run CI, CodeQL, Dependency Review, OpenSSF Scorecard, GitHub Pages deployment, and the manual release workflow. Dependabot tracks Maven and GitHub Actions updates. The Pages workflow publishes generated Javadocs under `/javadocs/`. SpotBugs and OWASP Dependency-Check are optional Maven profiles so normal CI stays fast. See [Security Hardening](docs/security-hardening.md).

JaCoCo coverage is generated during `verify`. Read the HTML report at `target/site/jacoco/index.html`; use `target/site/jacoco/jacoco.xml` for Codecov or Sonar if those services are configured later. No coverage badge is shown until a real external coverage service is configured.

Before deleting code, follow [Safe Code Cleanup](docs/code-cleanup.md): distinguish internal code from public API, search source/tests/docs/examples, inspect coverage, run Javadocs, and document user-visible removals in `CHANGELOG.md`. Before release, run the normal build, Javadocs, coverage generation, compile-scope dependency check, and any relevant optional quality/security profiles.

## License

Apache License 2.0. Copyright 2026 Arthur Hoch. See [LICENSE](LICENSE).
