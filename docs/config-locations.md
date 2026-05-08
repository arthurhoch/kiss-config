# Config Locations

Supported location types:

- `CLASSPATH`
- `CLASSPATH_LIBRARIES`
- `JAR_DIRECTORY`
- `WORKING_DIRECTORY`
- `EXPLICIT_PATH`
- `EXPLICIT_FILE`
- `ENV_FILE`
- `SYSTEM_PROPERTIES`
- `ENVIRONMENT_VARIABLES`

Factories:

```java
ConfigLocation.classpath();
ConfigLocation.classpathLibraries();
ConfigLocation.jarDirectory();
ConfigLocation.workingDirectory();
ConfigLocation.path("/opt/app");
ConfigLocation.file("/opt/app/application.properties");
ConfigLocation.envFile();
ConfigLocation.envFile("/run/secrets/myapp.env");
ConfigLocation.systemProperties();
ConfigLocation.environmentVariables();
```

`ConfigLocation.path(...)` means a directory. `ConfigLocation.file(...)` means one explicit file. `ConfigLocation.envFile(...)` means one explicit env file. `ConfigLocation.envFile()` means the env file configured through the builder.

Directories do not load `.env` automatically. Env file loading must be configured.

Auto-discovered missing files are skipped. Explicit missing files fail with `ConfigLoadException` unless `.ignoreMissingExplicitFiles(true)` is set.

When `ConfigLocation.envFile()` reads a builder-configured env file with a profile, the configured base env file is explicit and the generated profile variant is optional.

Classpath libraries contribute defaults only from `META-INF/kiss-config/defaults.properties`.

`jarDirectory()` resolves the directory containing the running application JAR when possible. In IDE or test runs it may be skipped and reported.
