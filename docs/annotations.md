---
layout: default
---

# Annotations

Annotations live under `io.github.arthurhoch.kissconfig.annotations`.

They are applied to record components in v0.1.0. Field targets exist on the annotations for future compatibility, but normal class mapping is not supported yet.

## @ConfigName

Changes the key used for a record component.

If the value contains a dot, it is treated as a full key:

```java
public record ServerConfig(
        @ConfigName("server.port") int port
) {}
```

If the value has no dot, it is treated as a local component name and joined with the current nested prefix:

```java
public record ServerConfig(
        @ConfigName("bind-port") int port
) {}
```

`bind-port` normalizes to `bind.port`.

## @DefaultValue

Used when no config value exists:

```java
public record ServerConfig(
        @DefaultValue("8080") int port
) {}
```

The default string is interpolated and converted like a loaded value:

```java
public record ServerConfig(
        @DefaultValue("${SERVER_PORT:8080}") int port
) {}
```

## @Required

Missing object and wrapper values normally map to `null`. `@Required` changes that to a failure:

```java
public record DatabaseConfig(
        @Required String url
) {}
```

Missing primitive values already fail unless `@DefaultValue` is present.

## @Secret

Marks mapped values as secret in reports and exceptions:

```java
public record DatabaseConfig(
        @Secret String password
) {}
```

Use `@Secret` for any sensitive value whose key is not automatically detected as secret-looking.
