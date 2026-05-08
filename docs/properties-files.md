# Properties Files

KissConfig parses `.properties` files with `java.util.Properties` through a UTF-8 reader.

This keeps v0.1.0 compatible with standard Java properties syntax while preserving zero production dependencies.

## Supported Syntax

Standard `Properties` behavior is supported, including comments and common separators:

```properties
# comment
server.host=0.0.0.0
server.port:8080
server.name app
```

Keys and values are read as strings before normalization, merge, interpolation, and mapping.

## Profile Files

With `.propertyFile("application.properties")` and `.profile("prod")`, directory and classpath locations attempt:

```text
application.properties
application-prod.properties
```

With `myapp.properties`:

```text
myapp.properties
myapp-prod.properties
```

## Key Normalization

Keys are normalized after parsing:

```properties
SERVER_PORT=8080
server.port=9090
server-port=7070
```

All three normalize to `server.port`.

Use `MergeStrategy.FAIL_ON_DUPLICATE` if collisions should fail.

## Errors

Parse errors are wrapped in `ConfigParseException` with source metadata when available.
