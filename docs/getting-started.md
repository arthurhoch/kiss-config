# Getting Started

KissConfig needs Java 17 or newer and has no production dependencies.

## Basic Load

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

By default KissConfig searches for `application.properties` through `SearchOrder.defaults()`, normalizes keys, merges with `FILL_MISSING_ONLY`, interpolates values, and maps to the record.

## Explicit Builder

```java
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.SearchOrder;

AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mapTo(AppConfig.class);
```

Use `.load(AppConfig.class)` when you need the report and source metadata.

## Records Only In v0.1.0

Records are supported:

```java
public record DatabaseConfig(String url, String username, String password) {}
```

Normal JavaBeans and field-based classes are not supported yet. See [Mapping](mapping.md) and [Roadmap](roadmap.md).

## External Values

With default `FILL_MISSING_ONLY`, earlier sources win. If external sources should win without using override mode, use:

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.externalFirst())
        .mapTo(AppConfig.class);
```

If later sources should win, opt in:

```java
import io.github.arthurhoch.kissconfig.MergeStrategy;

AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

## Env Files

`.env` files are opt-in:

```java
AppConfig config = KissConfig.builder()
        .envFile(".env")
        .mapTo(AppConfig.class);
```

Without `.envFile(...)` or `ConfigLocation.envFile(...)`, `.env` files are not loaded.
