# Mapping

KissConfig primarily maps to Java records.

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}
```

```properties
server.host=0.0.0.0
server.port=8080
```

Supported target types:

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

Booleans support `true/false`, `yes/no`, `on/off`, and `1/0`.

Durations support ISO-8601 values such as `PT30S` and simple values such as `30s`, `5m`, `2h`, `1d`, and `500ms`.

Lists are comma-separated and trimmed. Comma escaping is not supported in v0.1.0.

Missing primitive values fail. Missing object or wrapper values map to `null` unless `@Required` is present. Nested records are mapped when keys exist under their prefix; otherwise missing nested records map to `null` unless required.
