---
layout: default
---

# Examples

These examples are written to compile against the v0.1.0 public API. Mapping targets are Java records unless a section explicitly says otherwise.

## A. Minimal Record Config

```java
import io.github.arthurhoch.kissconfig.KissConfig;

public record AppConfig(String name) {}

AppConfig config = KissConfig.load(AppConfig.class);
```

`application.properties`:

```properties
name=demo
```

## B. Nested Record Config

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

## C. Normal Java Classes

Normal JavaBeans, field-based classes, setter-based classes, and arbitrary constructors are not supported in v0.1.0.

This is intentionally unsupported:

```java
public final class AppConfig {
    private String name;

    public String getName() {
        return name;
    }
}
```

Use a record instead:

```java
public record AppConfig(String name) {}
```

Support for simple JavaBeans or field-based classes is listed in the roadmap.

## D. Production Config With Profile And Env File

```java
import io.github.arthurhoch.kissconfig.ConfigLocation;
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.MergeStrategy;
import io.github.arthurhoch.kissconfig.SearchOrder;

AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")))
        .mapTo(AppConfig.class);
```

The resolver attempts:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

With default `MergeStrategy.FILL_MISSING_ONLY`:

1. `/opt/app/application.properties` fills values first.
2. `/opt/app/application-prod.properties` only fills keys missing from base.
3. `/opt/app/.env` only fills keys still missing.
4. `/opt/app/.env.prod` only fills keys still missing.

With `MergeStrategy.OVERRIDE_EXISTING`, later files override earlier files:

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")))
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

## E. Explicit Env Path

```java
import io.github.arthurhoch.kissconfig.ConfigLocation;
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.SearchOrder;

AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env")))
        .mapTo(AppConfig.class);
```

`ConfigLocation.envFile("/run/secrets/myapp.env")` loads that exact env file. It does not automatically add a profile variant. If you want two explicit files, list both.

## F. System Properties

```bash
java -Dserver.port=9090 -jar app.jar
```

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.systemProperties()))
        .mapTo(AppConfig.class);
```

`server.port` stays `server.port`. `SERVER_PORT` also normalizes to `server.port`.

## G. Environment Variables

```bash
SERVER_PORT=9090 java -jar app.jar
```

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.environmentVariables()))
        .mapTo(AppConfig.class);
```

`SERVER_PORT` normalizes to `server.port`.

When external sources are combined with classpath or file sources, remember that default `FILL_MISSING_ONLY` means earlier sources win. Use `SearchOrder.externalFirst()` when external values should win under the default merge strategy.

## H. Interpolation

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
server.port=${SERVER_PORT:8080}
```

Interpolation runs after merge, so variables can be supplied by any source in the configured search order.

## I. Secret Masking

Automatic secret-looking keys are masked:

```properties
database.password=s3cr3t
jwt.secret=abc123
```

Annotation-based masking:

```java
import io.github.arthurhoch.kissconfig.annotations.Secret;

public record DatabaseConfig(
        String url,
        @Secret String password
) {}
```

Reports and conversion exceptions display masked values as `******`.

## J. Fail On Duplicate

```java
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.MergeStrategy;

AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FAIL_ON_DUPLICATE)
        .mapTo(AppConfig.class);
```

If two sources define keys that normalize to the same canonical key, loading fails with `ConfigDuplicateKeyException`.

Example collision:

```properties
SERVER_PORT=8080
server.port=9090
```

Both normalize to `server.port`.

## K. Report Usage

```java
import io.github.arthurhoch.kissconfig.KissConfig;
import io.github.arthurhoch.kissconfig.KissConfigResult;

KissConfigResult<AppConfig> result = KissConfig.builder()
        .envFile(".env")
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

Reports include attempted sources, loaded sources, skipped optional sources, effective values, source metadata, merge strategy, profile, configured file names, search order, and masked secrets.

## Annotations Usage

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

`@ConfigName` values containing a dot are full keys. Values without dots are local component names.
