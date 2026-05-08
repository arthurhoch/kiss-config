# Search Order

`SearchOrder` is read order. It is not priority by name.

The active `MergeStrategy` decides which value becomes effective when two sources define the same normalized key.

| Merge Strategy | Source order A -> B | Effective value when both define same key |
|---|---|---|
| `FILL_MISSING_ONLY` | A then B | A |
| `OVERRIDE_EXISTING` | A then B | B |
| `FAIL_ON_DUPLICATE` | A then B | error |

## FILL_MISSING_ONLY

`FILL_MISSING_ONLY` is the default.

Earlier sources fill values first. Later sources only add keys that are still missing.

| Read order | Source | `server.port` |
|---|---|---|
| 1 | `application.properties` | `8080` |
| 2 | environment variable `SERVER_PORT` | `9090` |
| Effective | | `8080` |

## OVERRIDE_EXISTING

`OVERRIDE_EXISTING` is opt-in.

Later sources replace earlier values.

| Read order | Source | `server.port` |
|---|---|---|
| 1 | `application.properties` | `8080` |
| 2 | environment variable `SERVER_PORT` | `9090` |
| Effective | | `9090` |

## FAIL_ON_DUPLICATE

`FAIL_ON_DUPLICATE` is opt-in. It throws `ConfigDuplicateKeyException` when a canonical key appears more than once across sources.

Use it for auditing, tests, and deployments where duplicate configuration is considered a mistake.

## Presets

`SearchOrder.defaults()`:

1. `ConfigLocation.classpathLibraries()`
2. `ConfigLocation.classpath()`
3. `ConfigLocation.jarDirectory()`
4. `ConfigLocation.workingDirectory()`
5. `ConfigLocation.systemProperties()`
6. `ConfigLocation.environmentVariables()`

The default order does not include `ConfigLocation.envFile()`, but directory locations load env files when the builder has `.envFile(...)`.

`SearchOrder.externalFirst()`:

1. `ConfigLocation.environmentVariables()`
2. `ConfigLocation.systemProperties()`
3. `ConfigLocation.envFile()`
4. `ConfigLocation.workingDirectory()`
5. `ConfigLocation.jarDirectory()`
6. `ConfigLocation.classpath()`
7. `ConfigLocation.classpathLibraries()`

`externalFirst()` is designed for the default `FILL_MISSING_ONLY` mode. Because external sources are read first, they have priority under the default merge behavior.

With `OVERRIDE_EXISTING`, later sources win. In that mode, `externalFirst()` no longer means external values win; classpath library defaults are read last and could override earlier external values if they define the same key.

`SearchOrder.production()` uses the same order as `externalFirst()`. It does not hardcode `/etc` paths, does not automatically enable profile `prod`, and does not automatically load `.env` unless `.envFile(...)` is configured.

`SearchOrder.classpathOnly()`:

1. `ConfigLocation.classpathLibraries()`
2. `ConfigLocation.classpath()`

`SearchOrder.test()`:

1. `ConfigLocation.workingDirectory()`
2. `ConfigLocation.classpath()`

`SearchOrder.none()` reads no sources.

## ExternalFirst Example

```java
AppConfig config = KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.externalFirst())
        .mapTo(AppConfig.class);
```

Under default `FILL_MISSING_ONLY`, environment variables are read before `.env`, working directory files, and classpath files, so environment variables win conflicts.

```text
SERVER_PORT=9090
application.properties: server.port=8080
effective server.port=9090
```

If the same builder uses `OVERRIDE_EXISTING`, later sources win:

```text
SERVER_PORT=9090
application.properties: server.port=8080
effective server.port=8080
```
