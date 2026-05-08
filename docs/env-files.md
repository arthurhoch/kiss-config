---
layout: default
---

# Env Files

`.env` files are opt-in. KissConfig never loads `.env` unless `.envFile(...)` is configured on the builder or `ConfigLocation.envFile(...)` is explicitly present in the search order.

This does not load `.env`:

```java
AppConfig config = KissConfig.load(AppConfig.class);
```

This enables env files for directory-based locations:

```java
AppConfig config = KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.of(ConfigLocation.workingDirectory()))
        .mapTo(AppConfig.class);
```

This loads one explicit env file path:

```java
AppConfig config = KissConfig.builder()
        .searchOrder(SearchOrder.of(ConfigLocation.envFile("/run/secrets/myapp.env")))
        .mapTo(AppConfig.class);
```

`ConfigLocation.envFile(path)` loads exactly that path and does not add profile variants automatically.

## Supported Format

KissConfig supports a small bash-style `.env` subset:

```dotenv
# comment
VAR1=teste
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/app
export FEATURE_ENABLED=true
QUOTED="hello\nworld"
SINGLE='literal value'
```

Supported lines:

- empty lines
- comments starting with `#`
- `KEY=value`
- `KEY="value"`
- `KEY='value'`
- optional `export KEY=value`

Supported double-quoted escapes:

- `\n`
- `\t`
- `\r`
- `\"`
- `\\`
- `\=`

## Unsupported Shell Features

KissConfig does not execute shell commands and does not implement a shell parser.

Unsupported in v0.1.0:

- command substitution
- shell arithmetic
- sourcing other files
- variable assignment side effects
- complex shell interpolation

Use KissConfig interpolation (`${NAME}` and `${NAME:default}`) inside values after loading.

## Profile Expansion

With:

```java
KissConfig.builder()
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")));
```

Directory locations attempt:

```text
/opt/app/.env
/opt/app/.env.prod
```

Custom env file names use dash-before-extension profile expansion:

```text
myapp.env
myapp-prod.env
```

## Explicit Env Paths

`ConfigLocation.envFile("/run/secrets/myapp.env")` means one explicit env file. Missing explicit env files fail unless `ignoreMissingExplicitFiles(true)` is configured.

An explicit env path loads that exact path. It does not automatically add a profile variant. To use profile-specific explicit env files, list each path explicitly or use a directory location with builder `.envFile(...)`.

`ConfigLocation.envFile()` without a path is a placeholder for the env file configured by the builder. If the builder did not configure `.envFile(...)`, the location is skipped and reported.

Builder `.envFile(path)` also supports absolute paths:

```java
AppConfig config = KissConfig.builder()
        .envFile("/run/secrets/myapp.env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.workingDirectory()))
        .mapTo(AppConfig.class);
```

Because `/run/secrets/myapp.env` is absolute, directory resolution keeps it absolute. With profile `prod`, KissConfig attempts:

```text
/run/secrets/myapp.env
/run/secrets/myapp-prod.env
```

## OS Environment Variables

Operating-system environment variables are separate from `.env` files. They are loaded from `System.getenv()` through `ConfigLocation.environmentVariables()`.

```bash
SERVER_PORT=8080 java -jar app.jar
```

`SERVER_PORT` normalizes to `server.port`.

## Merge Interaction

Env files participate in the same read-order and merge rules as other sources.

With default `FILL_MISSING_ONLY`, earlier sources win. With `OVERRIDE_EXISTING`, later sources win. With `FAIL_ON_DUPLICATE`, duplicate canonical keys fail.
