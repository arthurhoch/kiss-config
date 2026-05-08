---
layout: default
---

# Architecture

KissConfig has a small deterministic loading pipeline:

1. `SearchOrder`
2. `ConfigLocation`
3. `ConfigSourceResolver`
4. `ConfigSource`
5. Parser
6. Normalized `ConfigMap`
7. `MergeStrategy`
8. `VariableInterpolator`
9. `ConfigMapper`
10. `TypeConverter`
11. Typed config object
12. `KissConfigResult` report

## Core Invariants

- `SearchOrder` is read order.
- Default merge is `MergeStrategy.FILL_MISSING_ONLY`.
- `OVERRIDE_EXISTING` and `FAIL_ON_DUPLICATE` are opt-in.
- `.env` files are opt-in.
- Java records are the supported mapping target in v0.1.0.
- Production code has zero external dependencies.
- Secrets are masked in reports and exceptions.

## Public API

The public API stays small and focuses on:

- `KissConfig`
- `KissConfigBuilder`
- `KissConfigResult`
- `SearchOrder`
- `ConfigLocation`
- `ConfigLocationType`
- `MergeStrategy`
- diagnostic metadata types
- annotations
- exceptions

## Internals

Most production code lives in internal packages:

- `internal.source`
- `internal.parse`
- `internal.normalize`
- merge behavior inside `internal.source`
- `internal.interpolate`
- `internal.map`
- `internal.convert`
- `internal.report`

Internal APIs are not stable user API.

## Architecture Rules

Binding architecture docs live under `.github/architecture/`. AI agents should also read `docs/AI_PROJECT_MANUAL.md` for a single-file project manual.
