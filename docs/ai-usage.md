---
layout: default
---

# AI Usage Guide

This page gives AI coding agents and maintainers a compact user-facing guide for using KissConfig correctly in consumer projects. For changes to this repository, `AGENTS.md`, `PROJECT_CONTEXT.md`, `docs/AI_PROJECT_MANUAL.md`, and `.github/ALL_MARKDOWN.md` remain authoritative.

## When To Use KissConfig

Use KissConfig when a Java 17+ project needs deterministic, framework-free configuration loading from `.properties`, explicit `.env` files, Java system properties, and environment variables into Java records.

```java
AppConfig config = KissConfig.load(AppConfig.class);
```

## Rules For Generated Code

- Use records as mapping targets.
- Treat `.env` files as opt-in; do not imply they are loaded automatically.
- Remember that `SearchOrder` is read order, not priority by name.
- Keep the default merge behavior as `MergeStrategy.FILL_MISSING_ONLY`.
- Do not add YAML, TOML, JSON, Spring, Quarkus, Jakarta, Bean Validation, cloud secret managers, or logging frameworks to use KissConfig.
- Do not log raw reports or exception text that could expose secrets without reviewing masking behavior and application policy.

## Recommended Boundary

Load configuration once at startup or at an explicit application boundary, then pass typed configuration records into the rest of the application.

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .profile("prod")
        .load(AppConfig.class);

AppConfig config = result.value();
```

## Related Projects

These libraries are independent, zero-dependency Java 17+ projects. Use only the modules you need.

| Project | Purpose |
|---|---|
| [kiss-json](https://github.com/arthurhoch/kiss-json) | Field-based JSON serialization and deserialization. |
| [kiss-requests](https://github.com/arthurhoch/kiss-requests) | Simple HTTP client built on Java HttpClient. |
| [kiss-server](https://github.com/arthurhoch/kiss-server) | Small HTTP/1.1 server for simple REST-style applications. |
| [kiss-config](https://github.com/arthurhoch/kiss-config) | Configuration loading from properties, .env files, system properties, and environment variables. |
| [kiss-binary](https://github.com/arthurhoch/kiss-binary) | Explicit binary IO for primitive binary formats. |
