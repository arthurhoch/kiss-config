# KissConfig AI Project Manual

## 1. Project Identity

- Project name: KissConfig
- Maven coordinates: `io.github.arthurhoch:kiss-config`
- Current project version: `0.1.1-SNAPSHOT`
- Java version: 17+
- Package root: `io.github.arthurhoch.kissconfig`
- Repository URL: `https://github.com/arthurhoch/kiss-config`
- License: Apache-2.0
- Dependency rule: zero production dependencies
- Release target: first public release `0.1.0`

## 2. Product Purpose

KissConfig loads configuration from `.properties` files, explicit `.env` files, Java system properties, and operating-system environment variables into immutable typed Java objects.

The primary target in v0.1.0 is Java records.

KissConfig exists to make this common configuration workflow small, deterministic, framework-free, and easy to audit:

```java
AppConfig config = KissConfig.load(AppConfig.class);
```

KissConfig does not try to be a full application framework. In v0.1.0 it does not provide YAML, TOML, JSON, hot reload, cloud secret manager integrations, Bean Validation, i18n, Spring, Quarkus, Jakarta, Lombok, or logging framework integration.

## 3. Core Philosophy

- KISS: keep the public API small and behavior predictable.
- Zero production dependencies: runtime code uses only the Java standard library.
- No frameworks: no Spring, Quarkus, Jakarta, Lombok, or framework assumptions.
- Typed object mapping only: users consume config as typed objects, not as a primary map API.
- Records preferred: records are the supported mapping target for v0.1.0.
- Predictable source order: `SearchOrder` is read order.
- Explicit env files: `.env` files are never loaded unless configured.
- Clear errors: failures should include the key, source, target type, safe value, and expected format when available.
- Secret masking: reports and exceptions must avoid leaking values for secret-looking keys and `@Secret` components.
- No YAML/TOML/JSON in core v0.1.0: those belong in future optional modules, if added.

## 4. Mental Model

Configuration flows through this pipeline:

```text
SearchOrder
-> ConfigLocation
-> ConfigSourceResolver
-> ConfigSource
-> Parser
-> Key Normalizer
-> ConfigMap
-> MergeStrategy
-> Interpolator
-> Mapper
-> TypeConverter
-> Java object
-> KissConfigResult/report
```

Important details:

- Source resolution happens before parsing.
- Keys are normalized to canonical dot form.
- Merging happens before interpolation.
- Interpolation happens before mapping.
- Mapping is the intended consumption model.
- Reports show the effective config map and source diagnostics with secrets masked.

## 5. Public API Summary

### KissConfig

Entry point:

```java
AppConfig config = KissConfig.load(AppConfig.class);
KissConfigBuilder builder = KissConfig.builder();
```

### KissConfigBuilder

Configures loading:

```java
KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .mergeStrategy(MergeStrategy.FILL_MISSING_ONLY)
        .ignoreMissingExplicitFiles(false)
        .mapTo(AppConfig.class);
```

`mapTo(Class<T>)` returns the mapped value.

`load(Class<T>)` returns `KissConfigResult<T>`.

### KissConfigResult

Contains:

- `value()`
- `configMap()`
- `sources()`
- `report()`

### SearchOrder

Defines read order:

- `SearchOrder.of(ConfigLocation...)`
- `SearchOrder.defaults()`
- `SearchOrder.externalFirst()`
- `SearchOrder.classpathOnly()`
- `SearchOrder.production()`
- `SearchOrder.test()`
- `SearchOrder.none()`

### ConfigLocation

Defines source locations:

- `classpath()`
- `classpathLibraries()`
- `jarDirectory()`
- `workingDirectory()`
- `path(...)`
- `file(...)`
- `envFile()`
- `envFile(...)`
- `systemProperties()`
- `environmentVariables()`

### ConfigLocationType

Enum for location kinds:

- `CLASSPATH`
- `CLASSPATH_LIBRARIES`
- `JAR_DIRECTORY`
- `WORKING_DIRECTORY`
- `EXPLICIT_PATH`
- `EXPLICIT_FILE`
- `ENV_FILE`
- `SYSTEM_PROPERTIES`
- `ENVIRONMENT_VARIABLES`

### MergeStrategy

- `FILL_MISSING_ONLY`
- `OVERRIDE_EXISTING`
- `FAIL_ON_DUPLICATE`

### ConfigMap, ConfigValue, LoadedConfigSource

Diagnostic support returned from `KissConfigResult`.

- `ConfigMap`: effective normalized map after merge and interpolation.
- `ConfigValue`: one effective value with canonical key, raw key, source metadata, and secret flag.
- `LoadedConfigSource`: attempted source metadata, including loaded/skipped state.

The intended user consumption model remains typed object mapping. These types support reporting and diagnostics.

### Annotations

Package: `io.github.arthurhoch.kissconfig.annotations`

- `@ConfigName`
- `@DefaultValue`
- `@Required`
- `@Secret`

### Exceptions

Package: `io.github.arthurhoch.kissconfig.exceptions`

- `ConfigException`
- `ConfigLoadException`
- `ConfigParseException`
- `ConfigMappingException`
- `ConfigInterpolationException`
- `ConfigDuplicateKeyException`
- `ConfigMissingPropertyException`
- `ConfigInvalidProfileException`

## 6. Default Behavior

- Default property file: `application.properties`
- Default env file: none
- Default search order: `SearchOrder.defaults()`
- Default merge strategy: `MergeStrategy.FILL_MISSING_ONLY`
- Default profile: none
- Missing auto-discovered files: skipped
- Missing explicit files: fail unless `ignoreMissingExplicitFiles(true)` is configured
- Missing primitive record component: fail
- Missing wrapper/object record component: `null` unless `@Required`
- Missing nested record: `null` unless keys exist under the prefix or the component is `@Required`

`SearchOrder.defaults()` reads:

1. `ConfigLocation.classpathLibraries()`
2. `ConfigLocation.classpath()`
3. `ConfigLocation.jarDirectory()`
4. `ConfigLocation.workingDirectory()`
5. `ConfigLocation.systemProperties()`
6. `ConfigLocation.environmentVariables()`

The default order does not load `.env` files unless `.envFile(...)` is configured or an explicit `ConfigLocation.envFile(...)` is present.

## 7. SearchOrder Semantics

`SearchOrder` is read order.

It is not priority by name. Priority depends on `MergeStrategy`.

| Merge Strategy | Source order A -> B | Effective value when both define same key |
|---|---|---|
| `FILL_MISSING_ONLY` | A then B | A |
| `OVERRIDE_EXISTING` | A then B | B |
| `FAIL_ON_DUPLICATE` | A then B | error |

### FILL_MISSING_ONLY

Default. Earlier source wins:

```text
classpath application.properties: server.port=8080
environment variable SERVER_PORT=9090
effective server.port=8080
```

### OVERRIDE_EXISTING

Opt-in. Later source wins:

```text
classpath application.properties: server.port=8080
environment variable SERVER_PORT=9090
effective server.port=9090
```

### FAIL_ON_DUPLICATE

Opt-in. Repeated canonical keys fail. Use it for auditing and tests when duplicate configuration should be rejected.

### externalFirst()

`SearchOrder.externalFirst()` reads external sources before classpath sources:

1. environment variables
2. system properties
3. builder-configured env file placeholder
4. working directory
5. jar directory
6. classpath
7. classpath library defaults

This is designed for default `FILL_MISSING_ONLY`, where earlier sources have priority. Under `OVERRIDE_EXISTING`, later sources win, so `externalFirst()` no longer means external values win.

## 8. Config Locations

### classpath()

Reads application classpath resources for the configured property file and profile variant:

```java
SearchOrder.of(ConfigLocation.classpath())
```

With `application.properties` and profile `prod`, it checks:

```text
classpath:application.properties
classpath:application-prod.properties
```

### classpathLibraries()

Reads library defaults only from:

```text
META-INF/kiss-config/defaults.properties
```

It does not scan arbitrary dependency `application.properties` files.

### jarDirectory()

Reads from the directory containing the running application JAR when available. In IDE/test runs it can be skipped and shown in the report.

### workingDirectory()

Reads from `Path.of("").toAbsolutePath()`.

### path(...)

Treats the value as a directory:

```java
ConfigLocation.path("/opt/app")
```

Directory locations read property files and profile property files. They read env files only when env loading is configured.

### file(...)

Treats the value as one explicit file:

```java
ConfigLocation.file("/opt/app/application.properties")
```

Missing explicit files fail unless `ignoreMissingExplicitFiles(true)` is configured.

### envFile()

Placeholder for the env file configured by the builder:

```java
KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.of(ConfigLocation.envFile()))
        .mapTo(AppConfig.class);
```

With profile `prod`, the builder-configured base env file is explicit and the generated profile variant is optional.

The builder-configured env file can be a simple name or an absolute path. Absolute builder env paths remain absolute when used with directory locations, and profile expansion still applies.

### envFile(...)

Treats the value as one explicit env file:

```java
ConfigLocation.envFile("/run/secrets/myapp.env")
```

It loads that exact file. If you want a separate profile-specific explicit env file, include another explicit `ConfigLocation.envFile(...)`.

### systemProperties()

Loads `System.getProperties()`. Both `server.port` and `SERVER_PORT` normalize to `server.port`.

### environmentVariables()

Loads `System.getenv()`. Bash-style environment variables such as `SERVER_PORT=8080` normalize to `server.port`.

## 9. Profiles

KissConfig supports one profile in v0.1.0:

```java
.profile("prod")
```

Profile names must match:

```text
[a-zA-Z0-9_-]+
```

Filename expansion:

| Base file | Profile | Profile file |
|---|---|---|
| `application.properties` | `prod` | `application-prod.properties` |
| `.env` | `prod` | `.env.prod` |
| `myapp.properties` | `prod` | `myapp-prod.properties` |
| `myapp.env` | `prod` | `myapp-prod.env` |

For `/opt/app` with:

```java
KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")));
```

KissConfig attempts:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

Profiles are never enabled automatically. `SearchOrder.production()` does not imply `profile("prod")`.

## 10. Env Files

`.env` loading is opt-in.

This does not load `.env`:

```java
KissConfig.load(AppConfig.class);
```

This loads `.env` from directory locations:

```java
KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.of(ConfigLocation.workingDirectory()))
        .mapTo(AppConfig.class);
```

This loads one explicit env path:

```java
KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env")))
        .mapTo(AppConfig.class);
```

`ConfigLocation.envFile(path)` loads the exact path and does not add profile variants automatically. Builder `.envFile(path)` can also be absolute; directory locations keep absolute paths absolute and add the profile variant when a profile is active.

Supported `.env` syntax:

```dotenv
# comment
VAR1=teste
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/app
export FEATURE_ENABLED=true
QUOTED="hello\nworld"
SINGLE='literal value'
```

Double-quoted escapes:

- `\n`
- `\t`
- `\r`
- `\"`
- `\\`
- `\=`

Unsupported shell features:

- command execution
- shell arithmetic
- complex shell interpolation
- sourcing other files

OS environment variables are separate from `.env` files and are loaded only through `ConfigLocation.environmentVariables()`.

## 11. Properties Files

Properties files are parsed with `java.util.Properties` using UTF-8.

Supported standard behavior includes:

```properties
server.host=0.0.0.0
server.port:8080
server.name app
```

Keys are normalized after parsing, so these collide:

```properties
server.port=8080
SERVER_PORT=9090
```

Use `FAIL_ON_DUPLICATE` when duplicate canonical keys should fail.

## 12. Key Normalization

Keys normalize to canonical lower-case dot form:

| Input | Canonical key |
|---|---|
| `SERVER_PORT` | `server.port` |
| `server_port` | `server.port` |
| `server-port` | `server.port` |
| `server.port` | `server.port` |
| `database.pool-size` | `database.pool.size` |
| `DATABASE_POOL_SIZE` | `database.pool.size` |

Normalization rules:

- trim whitespace
- lower-case
- replace underscores and dashes with dots
- collapse duplicate dots
- remove leading and trailing dots
- preserve numeric segments

Collision risk: `SERVER_PORT`, `server_port`, `server-port`, and `server.port` all resolve to the same key.

## 13. Interpolation

Supported forms:

```properties
server.port=${SERVER_PORT}
server.host=${SERVER_HOST:0.0.0.0}
```

Defaults are supported:

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
```

Interpolation happens after all sources are merged. This lets one source reference values from another source regardless of whether the referenced source was read earlier or later.

Missing required variables throw `ConfigInterpolationException`.

Cycles are detected:

```properties
a=${b}
b=${a}
```

The exception includes the key and dependency chain. Secret values are masked where possible.

## 14. Mapping Model

### Records

Records are the supported and recommended target in v0.1.0:

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}
```

```properties
server.host=0.0.0.0
server.port=8080
```

### Normal Java classes

Normal JavaBeans, field-based classes, setters, and arbitrary constructors are not supported in v0.1.0. The mapper rejects non-record targets. This is intentional for the first release to keep mapping deterministic and the API small.

### Nested objects

Nested records map from dot-prefixed keys.

### Missing values

- Missing primitive component without `@DefaultValue`: error
- Missing wrapper/object component: `null`
- Missing `@Required` component: error
- Missing nested record: `null` unless keys exist under the prefix or the component is `@Required`

### Supported scalar and structured types

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

Durations support ISO-8601 such as `PT30S` and simple forms such as `30s`, `5m`, `2h`, `1d`, and `500ms`.

Lists are comma-separated and trimmed. Escaped commas are not supported in v0.1.0.

## 15. Annotations

### @ConfigName

Changes the config key for a record component.

If the value contains a dot, it is a full key:

```java
public record ServerConfig(@ConfigName("server.port") int port) {}
```

If the value has no dot, it is a local segment:

```java
public record ServerConfig(@ConfigName("bind-port") int port) {}
```

### @DefaultValue

Used when no config value exists:

```java
public record ServerConfig(@DefaultValue("8080") int port) {}
```

Defaults are interpolated and converted.

### @Required

Missing values fail even for nullable types:

```java
public record DatabaseConfig(@Required String url) {}
```

### @Secret

Masks values in reports and exceptions:

```java
public record DatabaseConfig(@Secret String password) {}
```

## 16. Error Model

All exceptions extend `ConfigException`.

- `ConfigLoadException`: source cannot be loaded, such as a missing explicit file.
- `ConfigParseException`: source cannot be parsed.
- `ConfigMappingException`: value cannot be mapped or target type is unsupported.
- `ConfigInterpolationException`: interpolation is missing, malformed, or cyclic.
- `ConfigDuplicateKeyException`: duplicate canonical key under `FAIL_ON_DUPLICATE`.
- `ConfigMissingPropertyException`: required value is missing.
- `ConfigInvalidProfileException`: profile name fails validation.

Errors should be actionable. Mapping errors should include the property, target type, source, safe value, and expected format where available.

## 17. Reporting

Use `load(Class<T>)` for diagnostics:

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .envFile(".env")
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

Reports include:

- property file
- env file
- profile
- merge strategy
- search order
- loaded sources
- skipped sources
- effective values
- source of each value
- masked secrets

## 18. Secret Masking

Secret values are masked when:

- a component is annotated with `@Secret`
- the key contains `password`, `passwd`, `secret`, `token`, or `credential`
- the key is or ends with `api.key`
- the key is or ends with `private.key`

Masked values render as:

```text
******
```

Reports and mapping/interpolation exceptions use the safe display value where possible.

## 19. Examples

### Simple record config

```java
public record AppConfig(String name) {}

AppConfig config = KissConfig.load(AppConfig.class);
```

```properties
name=demo
```

### Nested record config

```java
public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}
```

```properties
server.host=0.0.0.0
server.port=8080
```

### Normal Java class config

Not supported in v0.1.0:

```java
public class AppConfig {
    private String name;
}
```

Use records instead.

### Custom SearchOrder

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .searchOrder(SearchOrder.of(
                ConfigLocation.classpath(),
                ConfigLocation.workingDirectory(),
                ConfigLocation.systemProperties(),
                ConfigLocation.environmentVariables()
        ))
        .mapTo(AppConfig.class);
```

### Production profile

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")))
        .mapTo(AppConfig.class);
```

Attempts:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

### Explicit env path

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env")))
        .mapTo(AppConfig.class);
```

### System properties

```bash
java -Dserver.port=9090 -jar app.jar
```

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.systemProperties()))
        .mapTo(AppConfig.class);
```

### Environment variables

```bash
SERVER_PORT=9090 java -jar app.jar
```

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.environmentVariables()))
        .mapTo(AppConfig.class);
```

### Override existing

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

### Fail on duplicate

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FAIL_ON_DUPLICATE)
        .mapTo(AppConfig.class);
```

### Report usage

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .load(AppConfig.class);

System.out.println(result.report());
```

### Annotations

```java
public record ServerConfig(
        @ConfigName("server.port") int port,
        @DefaultValue("0.0.0.0") String host,
        @Required String name,
        @Secret String token
) {}
```

### Interpolation

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
```

## 20. Internal Architecture

Internal packages:

- `internal.source`: resolve and load sources.
- `internal.parse`: parse `.properties` and `.env`.
- `internal.normalize`: normalize keys.
- `internal.interpolate`: resolve `${NAME}` expressions.
- `internal.map`: map `ConfigMap` to records.
- `internal.convert`: convert strings to target types.
- `internal.report`: secret detection and report rendering.

Internal APIs are not public API. Do not document them as stable for users.

## 21. Testing Strategy

Tests should cover:

- search order presets
- config location factories
- profile filename expansion
- env opt-in behavior
- explicit env files
- key normalization
- properties parsing
- env parsing
- merge strategies
- interpolation defaults, missing values, and cycles
- nested record mapping
- missing primitive and nullable behavior
- annotations
- secret masking in reports and exceptions
- classpath library defaults
- zero production dependencies through Maven verification

Avoid tests that depend on the real machine environment. Use injected maps for system properties and environment variables.

## 22. Release Process

Current development version is `0.1.1-SNAPSHOT`.

Release version should be `0.1.0`.

Before release:

1. Ensure GitHub `main` is green.
2. Enable GitHub Pages from GitHub Actions.
3. Verify the `io.github.arthurhoch` namespace in Sonatype Central Portal.
4. Configure GitHub secrets:
   - `MAVEN_CENTRAL_USERNAME`
   - `MAVEN_CENTRAL_PASSWORD`
   - `GPG_PRIVATE_KEY`
   - `GPG_PASSPHRASE`
5. Run the manual release workflow with version `0.1.0`.
6. Review and publish the validated deployment in Central Portal if `autoPublish=false`.
7. Wait for Maven Central indexing.
8. Update `main` to the next development version, such as `0.1.2-SNAPSHOT`, after a future release.

Do not claim Maven Central availability before the artifact is actually published and indexed.

## 23. AI Agent Rules

Repository entry order for AI agents is defined in `AGENTS.md`. When a task points to this manual, treat it as the canonical single-file project context after reading `AGENTS.md`.

AI agents should normally read:

1. `AGENTS.md`
2. `PROJECT_CONTEXT.md`
3. `docs/AI_PROJECT_MANUAL.md`
4. `.github/ALL_MARKDOWN.md`
5. `README.md`
6. relevant architecture docs for the task

Rules:

- Do not add production dependencies.
- Update docs with behavior changes.
- Update tests with behavior changes.
- Keep README examples compiling.
- Run `mvn -B clean verify`.
- Run `mvn -B javadoc:javadoc` for public API or Javadoc changes.
- Check `mvn -B dependency:list -DincludeScope=compile` before release-sensitive work.
- Never leak secrets.
- Never weaken tests to make builds pass.
- Never claim a release exists before it exists.
- Do not add framework-specific code.
- Do not change a development `-SNAPSHOT` version to a release version except during the intentional release flow.

## 24. Known Limitations

v0.1.0 limitations:

- records only for mapping targets
- one profile only
- no hot reload
- no YAML/TOML/JSON in core
- no Bean Validation integration
- no custom conversion SPI
- no framework adapters
- no cloud secret manager integrations
- no logging framework
- no escaped commas in lists
- no complex shell parsing for `.env`

## 25. Roadmap

Possible future features:

- support simple JavaBeans/field-based classes
- multiple profiles
- watch/reload
- validation module
- YAML/TOML as optional modules
- conversion SPI
- framework adapters
- cloud secret manager adapters
