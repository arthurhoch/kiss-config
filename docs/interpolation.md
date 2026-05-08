# Interpolation

KissConfig supports variable interpolation in property and env values:

```properties
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
server.port=${SERVER_PORT:8080}
```

Supported forms:

- `${NAME}`
- `${NAME:default}`

Interpolation runs after all sources are merged. This allows values to reference variables loaded earlier or later in the configured search order.

Variables resolve against canonical keys and useful raw source keys. `DB_HOST`, `db.host`, and `db-host` all normalize to `db.host`.

Missing required variables throw `ConfigInterpolationException`. Cycles are detected and reported with the dependency chain.
