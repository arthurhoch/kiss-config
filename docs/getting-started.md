# Getting Started

KissConfig needs Java 17 or newer and has no production dependencies.

## Basic Load

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}

AppConfig config = KissConfig.load(AppConfig.class);
```

By default KissConfig searches for `application.properties` through `SearchOrder.defaults()`, then maps normalized keys to the record.

## Explicit Builder

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mapTo(AppConfig.class);
```

Use `.load(AppConfig.class)` when you need the report and source metadata.
