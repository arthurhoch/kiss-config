# KissConfig Project Context

KissConfig is a zero-production-dependency Java 17+ configuration library for loading `.properties` files, explicit `.env` files, Java system properties, and operating-system environment variables into typed Java objects.

Maven coordinates:

```text
io.github.arthurhoch:kiss-config
```

Current development version:

```text
0.1.0-SNAPSHOT
```

Repository:

```text
https://github.com/arthurhoch/kiss-config
```

Package root:

```text
io.github.arthurhoch.kissconfig
```

## Core Decisions

- Java 17+.
- Zero production dependencies.
- No Spring, Quarkus, Jakarta, Lombok, Bean Validation, logging framework, YAML, TOML, or JSON in core v0.1.0.
- Config maps to typed objects; this is the intended consumption model.
- Java records are the supported mapping target in v0.1.0.
- Normal JavaBeans/field-based classes are not supported yet.
- `SearchOrder` is read order.
- Default merge strategy is `FILL_MISSING_ONLY`.
- `OVERRIDE_EXISTING` and `FAIL_ON_DUPLICATE` are opt-in.
- `.env` files are opt-in and are never loaded unless `.envFile(...)` or `ConfigLocation.envFile(...)` is configured.
- Secrets are masked in reports and exceptions where possible.
- Do not claim Maven Central availability before the artifact is actually published.

## Public API Examples

Simple:

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}

AppConfig config = KissConfig.load(AppConfig.class);
```

Explicit:

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mergeStrategy(MergeStrategy.FILL_MISSING_ONLY)
        .mapTo(AppConfig.class);
```

Report:

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .envFile(".env")
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

## Source Order Rules

`SearchOrder` is read order, not priority by name.

| Merge Strategy | Source order A -> B | Effective value when both define same key |
|---|---|---|
| `FILL_MISSING_ONLY` | A then B | A |
| `OVERRIDE_EXISTING` | A then B | B |
| `FAIL_ON_DUPLICATE` | A then B | error |

`SearchOrder.externalFirst()` is designed for the default `FILL_MISSING_ONLY`, where earlier external sources have priority. With `OVERRIDE_EXISTING`, later sources win, so classpath values can override external values if classpath appears later.

## Merge Strategy Rules

- `FILL_MISSING_ONLY`: default, first source wins.
- `OVERRIDE_EXISTING`: opt-in, last source wins.
- `FAIL_ON_DUPLICATE`: opt-in, duplicate canonical keys throw `ConfigDuplicateKeyException`.

## Env Rules

`.env` is opt-in:

```java
KissConfig.builder().envFile(".env")
```

Explicit env path:

```java
SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env"))
```

`ConfigLocation.envFile(path)` loads that exact env file and does not add a profile variant. Builder `.envFile(path)` may also be an absolute path; directory locations resolve it as an absolute path and still add the profile variant when a profile is active.

Supported `.env` examples:

```dotenv
VAR1=teste
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/app
export FEATURE_ENABLED=true
QUOTED="hello\nworld"
```

No shell command execution or complex shell interpolation is supported.

## Profile Rules

One profile is supported:

```java
.profile("prod")
```

Profile names must match `[a-zA-Z0-9_-]+`.

Filename expansion:

- `application.properties` -> `application-prod.properties`
- `.env` -> `.env.prod`
- `myapp.properties` -> `myapp-prod.properties`
- `myapp.env` -> `myapp-prod.env`

For `/opt/app` with `propertyFile("application.properties")`, `envFile(".env")`, `profile("prod")`, and `ConfigLocation.path("/opt/app")`, files are attempted in this order:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

With default `FILL_MISSING_ONLY`, earlier files win. With `OVERRIDE_EXISTING`, later files win.

## Mapping Rules

Records are supported:

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}
```

Normal classes are not supported in v0.1.0.

Supported types include `String`, primitive/wrapper `int`, `long`, `double`, `boolean`, `BigDecimal`, `BigInteger`, `Duration`, `LocalDate`, `LocalTime`, `LocalDateTime`, `Instant`, enums, lists of supported scalar types, and arrays of supported scalar types.

Annotations:

- `@ConfigName`
- `@DefaultValue`
- `@Required`
- `@Secret`

## Release Rules

- Keep `0.1.0-SNAPSHOT` until the explicit release workflow.
- Do not publish to Maven Central outside the release workflow.
- Do not tag unless the human explicitly asks.
- Configure Sonatype Central Portal namespace `io.github.arthurhoch`.
- Configure GitHub secrets: `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `GPG_PRIVATE_KEY`, `GPG_PASSPHRASE`.
- Enable GitHub Pages from GitHub Actions.

## Verify

Run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

Compile-scope dependencies must remain `none`.
