---
layout: default
---

# Reporting

Use `KissConfigResult<T>` when you need diagnostics in addition to the mapped object.

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .envFile(".env")
        .searchOrder(SearchOrder.production())
        .load(AppConfig.class);

AppConfig config = result.value();
ConfigMap map = result.configMap();
List<LoadedConfigSource> sources = result.sources();
String report = result.report();
```

## Report Contents

Reports show:

- configured property file
- configured env file or `(none)`
- profile or `(none)`
- merge strategy
- search order
- loaded sources
- skipped missing optional sources
- skipped duplicate resolved sources
- effective values
- source of each effective value
- masked secrets

## Example Report

```text
KissConfig report
Property file: application.properties
Env file: .env
Profile: prod
Merge strategy: FILL_MISSING_ONLY
Search order:
  - EXPLICIT_PATH(/opt/app)
Sources:
  - LOADED /opt/app/application.properties [EXPLICIT_PATH] values=2
  - LOADED /opt/app/application-prod.properties [EXPLICIT_PATH] values=1
  - SKIPPED /opt/app/.env [EXPLICIT_PATH] reason=Missing optional source
  - SKIPPED /opt/app/.env.prod [EXPLICIT_PATH] reason=Missing optional source
Effective values:
  - database.password = ****** (source: /opt/app/application.properties)
  - server.host = 0.0.0.0 (source: /opt/app/application.properties)
  - server.port = 8080 (source: /opt/app/application.properties)
```

The exact skip messages can vary by source type, but secret values must stay masked.

## ConfigMap

`result.configMap()` returns the effective normalized map after merge and interpolation.

The map is useful for diagnostics and reports. User code should normally consume the typed record returned by `result.value()`.

## Source Metadata

`result.sources()` returns every attempted source with:

- name
- type
- optional path
- loaded/skipped flag
- optional flag
- explicit flag
- value count
- diagnostic message
