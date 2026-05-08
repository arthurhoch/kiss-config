# Profiles

KissConfig supports one profile in v0.1.0.

Profile names must match:

```text
[a-zA-Z0-9_-]+
```

Rejected examples include empty strings, `../prod`, `prod/test`, and `prod.properties`.

## Property Files

With `.propertyFile("application.properties")` and `.profile("prod")`, directory locations attempt:

```text
application.properties
application-prod.properties
```

With `myapp.properties`:

```text
myapp.properties
myapp-prod.properties
```

## Env Files

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

If prod or env values should override base values, configure `MergeStrategy.OVERRIDE_EXISTING`.
