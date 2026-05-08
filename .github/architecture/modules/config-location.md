# Config Location Module

Supported location types are classpath, classpath library defaults, jar directory, working directory, explicit directory, explicit file, explicit env file, system properties, and environment variables.

Directory locations discover property files and profile property files. They discover env files only when env loading is explicitly configured.

Explicit missing files fail by default. Auto-discovered missing files are skipped.

`ConfigLocation.path(...)` always means directory. `ConfigLocation.file(...)` always means one explicit file. `ConfigLocation.envFile(...)` always means one explicit env file. `ConfigLocation.envFile()` is only a placeholder for the builder-configured env file.

For `/opt/app` with `propertyFile("application.properties")`, `envFile(".env")`, and `profile("prod")`, a directory location attempts:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```
