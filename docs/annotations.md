# Annotations

Annotations live under `io.github.arthurhoch.kissconfig.annotations`.

## @ConfigName

Changes the key used for a record component.

If the value contains a dot, it is treated as a full key:

```java
public record ServerConfig(@ConfigName("server.port") int port) {}
```

If the value has no dot, it is treated as a local component name:

```java
public record ServerConfig(@ConfigName("bind-port") int port) {}
```

## @DefaultValue

Used when no config value exists. The value is interpolated and converted like any loaded value.

## @Required

Missing values fail even for object and wrapper types.

## @Secret

Marks mapped values as secret in reports and exceptions.
