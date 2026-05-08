# API

This is the compact public API reference for v0.1.0.

## KissConfig

Entry point:

```java
AppConfig config = KissConfig.load(AppConfig.class);
KissConfigBuilder builder = KissConfig.builder();
```

`load(Class<T>)` uses default builder options and returns only the mapped record.

## KissConfigBuilder

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

Builder methods:

- `propertyFile(String)`
- `propertyFile(Path)`
- `envFile(String)`
- `envFile(Path)`
- `profile(String)`
- `searchOrder(SearchOrder)`
- `mergeStrategy(MergeStrategy)`
- `ignoreMissingExplicitFiles(boolean)`
- `mapTo(Class<T>)`
- `load(Class<T>)`

`mapTo(Class<T>)` returns the mapped value. `load(Class<T>)` returns `KissConfigResult<T>`.

## KissConfigResult

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .load(AppConfig.class);

AppConfig config = result.value();
ConfigMap map = result.configMap();
List<LoadedConfigSource> sources = result.sources();
String report = result.report();
```

## SearchOrder

`SearchOrder` is read order.

Factory methods:

- `SearchOrder.of(ConfigLocation...)`
- `SearchOrder.defaults()`
- `SearchOrder.externalFirst()`
- `SearchOrder.classpathOnly()`
- `SearchOrder.production()`
- `SearchOrder.test()`
- `SearchOrder.none()`

See [Search Order](search-order.md).

## ConfigLocation

Factory methods:

- `ConfigLocation.classpath()`
- `ConfigLocation.classpathLibraries()`
- `ConfigLocation.jarDirectory()`
- `ConfigLocation.workingDirectory()`
- `ConfigLocation.path(String)`
- `ConfigLocation.path(Path)`
- `ConfigLocation.file(String)`
- `ConfigLocation.file(Path)`
- `ConfigLocation.envFile()`
- `ConfigLocation.envFile(String)`
- `ConfigLocation.envFile(Path)`
- `ConfigLocation.systemProperties()`
- `ConfigLocation.environmentVariables()`

`path(...)` means directory. `file(...)` means one explicit file. `envFile(...)` means one explicit env file. `envFile()` means the builder-configured env file placeholder.

Builder `envFile(String)` and `envFile(Path)` accept simple names and absolute paths. Directory locations resolve simple names relative to the directory, keep absolute paths absolute, and add profile variants when a profile is active.

See [Config Locations](config-locations.md).

## ConfigLocationType

Enum values:

- `CLASSPATH`
- `CLASSPATH_LIBRARIES`
- `JAR_DIRECTORY`
- `WORKING_DIRECTORY`
- `EXPLICIT_PATH`
- `EXPLICIT_FILE`
- `ENV_FILE`
- `SYSTEM_PROPERTIES`
- `ENVIRONMENT_VARIABLES`

## MergeStrategy

Enum values:

- `FILL_MISSING_ONLY`: default, first source wins.
- `OVERRIDE_EXISTING`: opt-in, last source wins.
- `FAIL_ON_DUPLICATE`: opt-in, duplicate canonical keys fail.

See [Merge Strategy](merge-strategy.md).

## ConfigMap And ConfigValue

`ConfigMap` is the effective normalized map returned in `KissConfigResult`.

Useful methods:

- `get(String)`
- `getValue(String)`
- `containsKey(String)`
- `valuesByKey()`
- `asMap()`
- `values()`

`ConfigValue` contains canonical key, raw key, value, source name/type/path, optional line number, and secret status.

The map is diagnostic support. The intended consumption model is typed object mapping.

## LoadedConfigSource

Describes attempted and loaded sources in `KissConfigResult.sources()`.

Metadata includes name, type, optional path, loaded/skipped status, optional/explicit flags, value count, and message.

## Annotations

Package: `io.github.arthurhoch.kissconfig.annotations`

- `@ConfigName`
- `@DefaultValue`
- `@Required`
- `@Secret`

See [Annotations](annotations.md).

## Exceptions

Package: `io.github.arthurhoch.kissconfig.exceptions`

- `ConfigException`
- `ConfigLoadException`
- `ConfigParseException`
- `ConfigMappingException`
- `ConfigInterpolationException`
- `ConfigDuplicateKeyException`
- `ConfigMissingPropertyException`
- `ConfigInvalidProfileException`

See [Errors](errors.md).
