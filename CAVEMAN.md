# KissConfig Caveman Summary

## What This Is

KissConfig is a zero-production-dependency Java 17+ configuration library.

It loads `.properties` files, explicit `.env` files, Java system properties, and operating-system environment variables into typed Java records.

## Main Mental Model

```java
AppConfig config = KissConfig.load(AppConfig.class);
```

Use the builder for explicit sources, profiles, merge strategy, and diagnostics:

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .searchOrder(SearchOrder.production())
        .load(AppConfig.class);
```

## KISS Rules

- Java 17+.
- Zero production dependencies.
- Records are the supported mapping target in v0.1.0.
- `.env` files are opt-in.
- `SearchOrder` is read order.
- Default merge is `MergeStrategy.FILL_MISSING_ONLY`.
- Keep reports and exceptions from exposing secret values.
- Do not add Spring, Quarkus, Jakarta, Lombok, Bean Validation, YAML, TOML, JSON, cloud secret managers, or logging frameworks to core.

## What This Is Not

Not a framework. Not a configuration server. Not a secrets manager. Not a schema engine. Not YAML/TOML/JSON configuration. Not a Bean Validation integration. Not a Spring or Quarkus adapter.

## Before Changing Behavior

Read:

1. `AGENTS.md`
2. `PROJECT_CONTEXT.md`
3. `docs/AI_PROJECT_MANUAL.md`
4. `.github/ALL_MARKDOWN.md`
5. `README.md`

Then make the smallest correct change, update docs and tests, keep `CHANGELOG.md` current, and run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```
