# Secrets

KissConfig masks secret values in reports and exceptions where possible.

Masked values are rendered as:

```text
******
```

## Annotation-Based Masking

Use `@Secret` on record components:

```java
import io.github.arthurhoch.kissconfig.annotations.Secret;

public record DatabaseConfig(
        String url,
        @Secret String password
) {}
```

`@Secret` masks the effective value in reports and conversion exceptions.

## Automatic Secret Detection

KissConfig also masks secret-looking canonical keys.

Automatic secret indicators:

- `password`
- `passwd`
- `secret`
- `token`
- `credential`
- `api.key`
- `private.key`

Examples:

```properties
database.password=s3cr3t
jwt.secret=abc123
github.token=ghp_example
service.api.key=abc
tls.private.key=-----BEGIN PRIVATE KEY-----
```

All of these are displayed as `******` in reports.

## Exceptions

When a secret value cannot be converted, the exception uses the masked safe value:

```text
Cannot map property 'database.password' to int.
Value: "******"
Source: /opt/app/application.properties
Expected: integer number
```

Do not log raw config values from your own code unless you know they are safe.

## Limits

Secret detection is defensive, not a full data-loss-prevention system. Prefer `@Secret` for any sensitive record component whose key is not obviously secret-looking.
