---
layout: default
---

# Mapping

KissConfig maps the merged and interpolated `ConfigMap` to a Java object. In v0.1.0, Java records are the supported target type.

## Records

Records are recommended because they are immutable, constructor-bound, and explicit.

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

```java
AppConfig config = KissConfig.load(AppConfig.class);
```

## Normal Java Classes

Normal JavaBeans, field-based classes, setters, and arbitrary constructors are not supported in v0.1.0.

The mapper rejects non-record target types with `ConfigMappingException`.

This is intentional for the first release. It keeps mapping deterministic and avoids a larger reflection surface. Support for simple JavaBeans or field-based classes is tracked in the roadmap.

## Constructor And Field Behavior

For records, KissConfig reads record components and calls the canonical record constructor.

It does not set fields directly. It does not call setters. It does not bind arbitrary constructors.

## Nested Records

Nested records map from dot-prefixed keys:

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}
```

```properties
server.host=0.0.0.0
server.port=8080
```

If any keys exist under the nested prefix, KissConfig maps the nested record. If no keys exist, the nested record component is `null` unless it is `@Required`.

## Missing Values

| Target component | Missing behavior |
|---|---|
| primitive without `@DefaultValue` | `ConfigMissingPropertyException` |
| wrapper/object without `@Required` | `null` |
| wrapper/object with `@Required` | `ConfigMissingPropertyException` |
| nested record with keys under prefix | nested mapping |
| nested record without keys | `null` unless `@Required` |

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
- `List<String>`
- `List<Integer>`
- `List<Long>`
- `List<Boolean>`
- arrays of supported scalar types

## Booleans

Accepted values:

- `true`, `false`
- `yes`, `no`
- `on`, `off`
- `1`, `0`

## Durations

Accepted values:

- ISO-8601 durations, such as `PT30S`
- simple durations:
  - `500ms`
  - `30s`
  - `5m`
  - `2h`
  - `1d`

## Dates And Times

KissConfig delegates to Java time parsers:

- `LocalDate`: `2026-05-06`
- `LocalTime`: `10:15:30`
- `LocalDateTime`: `2026-05-06T10:15:30`
- `Instant`: `2026-05-06T10:15:30Z`

## Enums

Enum conversion is case-insensitive and treats dashes as underscores.

```java
enum Mode {
    DEV,
    PROD
}

public record AppConfig(Mode mode) {}
```

```properties
mode=prod
```

## Lists And Arrays

Lists are comma-separated and trimmed:

```properties
allowed.origins=https://a.example, https://b.example
```

```java
public record CorsConfig(List<String> allowedOrigins) {}
```

Escaped commas are not supported in v0.1.0.

Arrays are supported for scalar component types:

```java
public record TagsConfig(String[] tags) {}
```

## Annotations

Mapping supports record-component annotations:

```java
public record ServerConfig(
        @ConfigName("server.host") String host,
        @DefaultValue("8080") int port,
        @Required String name,
        @Secret String token
) {}
```

- `@ConfigName`: changes the config key.
- `@DefaultValue`: supplies a string default that is interpolated and converted.
- `@Required`: fails when a nullable value is missing.
- `@Secret`: marks the value secret for reports and exceptions.

## Unsupported Targets

Unsupported in v0.1.0:

- JavaBeans
- field injection
- setter injection
- arbitrary constructors
- maps as direct mapping targets
- collections as root mapping targets
- custom conversion SPI
- Bean Validation integration
