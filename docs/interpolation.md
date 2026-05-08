# Interpolation

KissConfig supports variable interpolation in property and env values.

Supported forms:

- `${NAME}`
- `${NAME:default}`

Example:

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
server.port=${SERVER_PORT:8080}
```

## After Merge

Interpolation runs after all sources are loaded, normalized, and merged.

This matters because a value in one source can reference a variable from another source regardless of read order:

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:5432/app
```

```dotenv
DB_HOST=db.internal
```

The merged config map is the interpolation input.

## Name Resolution

Variables resolve against canonical keys and useful raw source keys.

These are equivalent for lookup:

- `DB_HOST`
- `db.host`
- `db-host`
- `db_host`

All normalize to `db.host`.

## Defaults

`${NAME:default}` uses the default string when `NAME` is missing.

Defaults can also contain interpolation:

```properties
database.host=${DB_HOST:${FALLBACK_DB_HOST:localhost}}
```

## Missing Variables

`${NAME}` without a default fails when the variable is missing:

```text
ConfigInterpolationException
```

The exception includes the key, expression, source when available, and dependency chain.

## Cycles

Cycles are detected:

```properties
a=${b}
b=${a}
```

This throws `ConfigInterpolationException`.

## Secrets

If interpolation fails on a secret-looking key or an `@Secret` value, diagnostics use the safe display value where possible.
