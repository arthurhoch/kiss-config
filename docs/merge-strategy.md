# Merge Strategy

`MergeStrategy` controls how values from later sources interact with values already loaded from earlier sources.

`SearchOrder` supplies read order. `MergeStrategy` supplies conflict behavior.

## FILL_MISSING_ONLY

`FILL_MISSING_ONLY` is the default.

Earlier sources fill values first. Later sources only add missing keys.

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FILL_MISSING_ONLY)
        .mapTo(AppConfig.class);
```

Example:

| Source order | Value |
|---|---|
| `application.properties` | `server.port=8080` |
| `.env` | `SERVER_PORT=9090` |
| Effective | `server.port=8080` |

The report shows the source of the effective value. Values ignored by merge are not effective values, but their source may still appear in the loaded source list.

## OVERRIDE_EXISTING

`OVERRIDE_EXISTING` must be configured explicitly.

Later sources replace earlier values.

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

Example:

| Source order | Value |
|---|---|
| `application.properties` | `server.port=8080` |
| `.env` | `SERVER_PORT=9090` |
| Effective | `server.port=9090` |

The report source for `server.port` points to the later source that won.

## FAIL_ON_DUPLICATE

`FAIL_ON_DUPLICATE` must be configured explicitly.

If the same canonical key appears more than once across sources, loading fails with `ConfigDuplicateKeyException`.

```java
AppConfig config = KissConfig.builder()
        .mergeStrategy(MergeStrategy.FAIL_ON_DUPLICATE)
        .mapTo(AppConfig.class);
```

Example:

```properties
SERVER_PORT=8080
server.port=9090
```

Both keys normalize to `server.port`, so duplicate mode fails. This strategy is useful for release audits, tests, and deployments where accidental shadowing should be rejected.

## Strategy Table

| Strategy | Default | Conflict behavior | Typical use |
|---|---:|---|---|
| `FILL_MISSING_ONLY` | yes | first source wins | stable defaults, explicit read-order priority |
| `OVERRIDE_EXISTING` | no | last source wins | profile/env override workflows |
| `FAIL_ON_DUPLICATE` | no | duplicate canonical key fails | audits and strict tests |
