# Config Locations

`ConfigLocation` describes one place KissConfig can read from. Locations are placed in a `SearchOrder`, and the order is read order.

## Location Types

- `CLASSPATH`
- `CLASSPATH_LIBRARIES`
- `JAR_DIRECTORY`
- `WORKING_DIRECTORY`
- `EXPLICIT_PATH`
- `EXPLICIT_FILE`
- `ENV_FILE`
- `SYSTEM_PROPERTIES`
- `ENVIRONMENT_VARIABLES`

## Factories

```java
ConfigLocation.classpath();
ConfigLocation.classpathLibraries();
ConfigLocation.jarDirectory();
ConfigLocation.workingDirectory();
ConfigLocation.path("/opt/app");
ConfigLocation.path(Path.of("/opt/app"));
ConfigLocation.file("/opt/app/application.properties");
ConfigLocation.file(Path.of("/opt/app/application.properties"));
ConfigLocation.envFile();
ConfigLocation.envFile("/run/secrets/myapp.env");
ConfigLocation.envFile(Path.of("/run/secrets/myapp.env"));
ConfigLocation.systemProperties();
ConfigLocation.environmentVariables();
```

## classpath()

Reads application classpath property resources for the configured property file and profile variant.

With `application.properties` and profile `prod`:

```text
classpath:application.properties
classpath:application-prod.properties
```

## classpathLibraries()

Reads library defaults only from:

```text
META-INF/kiss-config/defaults.properties
```

KissConfig does not scan arbitrary dependency `application.properties` files.

## jarDirectory()

Reads from the directory containing the running application JAR when possible.

When running from classes in an IDE or test process, this location is skipped and reported instead of failing.

## workingDirectory()

Reads from:

```java
Path.of("").toAbsolutePath()
```

## path(...)

`ConfigLocation.path(...)` means directory.

```java
ConfigLocation.path("/opt/app")
```

Directory locations attempt configured property files and profile property files. They load env files only when env loading was explicitly configured through builder `.envFile(...)`.

For `/opt/app` with `propertyFile("application.properties")`, `envFile(".env")`, and `profile("prod")`:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

Auto-discovered missing files are skipped.

## file(...)

`ConfigLocation.file(...)` means one explicit file.

```java
ConfigLocation.file("/opt/app/application.properties")
```

Missing explicit files fail with `ConfigLoadException` unless `.ignoreMissingExplicitFiles(true)` is configured.

The file format is inferred from the file name. Env-looking files such as `.env`, `.env.prod`, and `myapp.env` are parsed as env files; other files are parsed as properties files.

## envFile()

`ConfigLocation.envFile()` is a placeholder for the env file configured on the builder:

```java
AppConfig config = KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.of(ConfigLocation.envFile()))
        .mapTo(AppConfig.class);
```

If no builder env file is configured, this location is skipped and reported.

With profile `prod`, the builder-configured base env file is explicit and the generated profile variant is optional:

```text
.env
.env.prod
```

The builder env file can be a simple name or an absolute path. For an absolute builder env file such as `/run/secrets/myapp.env`, directory locations resolve the env file as the absolute path and still add a profile variant when a profile is active:

```text
/run/secrets/myapp.env
/run/secrets/myapp-prod.env
```

## envFile(...)

`ConfigLocation.envFile(...)` means one explicit env file.

```java
ConfigLocation.envFile("/run/secrets/myapp.env")
```

It loads exactly that file. It does not automatically add a profile variant. Missing explicit env files fail unless `.ignoreMissingExplicitFiles(true)` is configured.

## systemProperties()

Loads Java system properties from `System.getProperties()`.

Examples:

```bash
java -Dserver.port=9090 -jar app.jar
java -DSERVER_PORT=9090 -jar app.jar
```

Both keys normalize to `server.port`.

## environmentVariables()

Loads operating-system environment variables from `System.getenv()`.

```bash
SERVER_PORT=9090 java -jar app.jar
```

`SERVER_PORT` normalizes to `server.port`.

## Deduplication

Resolved files are deduplicated by canonical or normalized absolute path where possible. Duplicate resolved files are skipped and shown in the report.
