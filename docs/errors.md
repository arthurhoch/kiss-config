---
layout: default
---

# Errors

All KissConfig exceptions extend `ConfigException`.

Errors are intended to be actionable. When available, they include the key, source, target type, safe display value, and expected format.

## Exception Types

| Exception | When it happens |
|---|---|
| `ConfigException` | Base runtime exception for KissConfig failures |
| `ConfigLoadException` | A source cannot be loaded, such as a missing explicit file |
| `ConfigParseException` | A `.properties` or `.env` source cannot be parsed |
| `ConfigMappingException` | A value cannot be converted or target type is unsupported |
| `ConfigInterpolationException` | Interpolation is missing, malformed, or cyclic |
| `ConfigDuplicateKeyException` | Duplicate canonical key under `FAIL_ON_DUPLICATE` |
| `ConfigMissingPropertyException` | Required or primitive value is missing |
| `ConfigInvalidProfileException` | Profile name fails validation |

## Mapping Error Example

```text
Cannot map property 'server.port' to int.
Value: "abc"
Source: /opt/app/application.properties
Expected: integer number
```

For secret keys or `@Secret` components, the value is masked:

```text
Cannot map property 'database.password' to int.
Value: "******"
Source: /opt/app/application.properties
Expected: integer number
```

## Missing Property Example

```text
Missing required configuration property 'server.port' for target AppConfig.server.port.
```

Missing primitives fail unless `@DefaultValue` is present. Missing object and wrapper components map to `null` unless `@Required` is present.

## Interpolation Error Examples

Missing variable:

```text
Missing interpolation variable '${DB_HOST}' for key 'database.url'. Dependency chain: [database.url]
```

Cycle:

```text
Circular interpolation reference for key 'host'. Dependency chain: [host, port] -> host
```

Unclosed expression:

```text
Unclosed interpolation expression in value for key 'database.url'.
```

## Duplicate Key Example

With `MergeStrategy.FAIL_ON_DUPLICATE`:

```text
Duplicate configuration key 'server.port' from source /opt/app/application-prod.properties.
```

The exact message may include the source names involved. The key is canonical, so raw keys such as `SERVER_PORT` and `server.port` can collide.

## Profile Error Example

```text
Invalid profile '../prod'. Profile names must match [a-zA-Z0-9_-]+.
```

## Programmatic Metadata

`ConfigException` exposes optional metadata:

- `key()`
- `source()`
- `targetType()`
- `safeValue()`

Use these for diagnostics without parsing message text.
