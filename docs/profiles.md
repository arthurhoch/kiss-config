# Profiles

KissConfig supports one active profile in v0.1.0.

```java
AppConfig config = KissConfig.builder()
        .profile("prod")
        .mapTo(AppConfig.class);
```

Profiles are never enabled automatically. `SearchOrder.production()` does not imply `profile("prod")`; it only changes source read order.

## Validation

Profile names must match:

```text
[a-zA-Z0-9_-]+
```

Rejected examples:

- empty string
- `../prod`
- `prod/test`
- `prod.properties`

Invalid profiles throw `ConfigInvalidProfileException` before loading starts.

## Property File Expansion

With `.propertyFile("application.properties")` and `.profile("prod")`, directory and classpath locations attempt:

```text
application.properties
application-prod.properties
```

With `myapp.properties`:

```text
myapp.properties
myapp-prod.properties
```

## Env File Expansion

Env files are still opt-in.

With `.envFile(".env")` and `.profile("prod")`, directory locations attempt:

```text
.env
.env.prod
```

With `myapp.env`:

```text
myapp.env
myapp-prod.env
```

## Production Example

Given:

```java
KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.of(ConfigLocation.path("/opt/app")));
```

The resolver attempts:

```text
/opt/app/application.properties
/opt/app/application-prod.properties
/opt/app/.env
/opt/app/.env.prod
```

Because default `MergeStrategy.FILL_MISSING_ONLY` is used:

- `/opt/app/application.properties` fills first.
- `/opt/app/application-prod.properties` only fills keys missing from base.
- `/opt/app/.env` only fills keys still missing.
- `/opt/app/.env.prod` only fills keys still missing.

If profile or env files should override base values, configure:

```java
.mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
```

## SearchOrder.production()

`SearchOrder.production()` is an external-first read order. It does not:

- set `profile("prod")`
- enable `.env` loading by itself
- add `/etc` or any other fixed path

Configure those choices explicitly.
