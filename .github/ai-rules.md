# AI Rules

KissConfig is intentionally small. AI-generated changes must keep the library zero-dependency in production and Java 17 compatible.

## Required Reading

1. `README.md`
2. `.github/ALL_MARKDOWN.md`
3. `.github/architecture/index.md`

## Non-Negotiables

- No production dependencies.
- No Spring, Quarkus, Jakarta, Lombok, YAML, TOML, JSON parser, logging framework, Bean Validation, or cloud secret manager integration in v0.1.0.
- Do not add dependencies without explicit human approval.
- Keep public API changes small and documented.
- Add or update tests for behavior changes.
- Add or update docs for behavior changes.
- Keep secret values masked in reports and exceptions.
- Run `mvn -B clean verify` before claiming completion.

## Code Style

- Prefer records in examples and tests.
- Keep internals under `io.github.arthurhoch.kissconfig.internal`.
- Keep errors actionable and deterministic.
- Avoid broad refactors during focused behavior changes.
