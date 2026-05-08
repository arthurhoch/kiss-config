# AI Rules

KissConfig is intentionally small. AI-generated changes must keep the library zero-dependency in production, Java 17 compatible, deterministic, and framework-free.

## Required Reading

1. `AGENTS.md`
2. `PROJECT_CONTEXT.md`
3. `docs/AI_PROJECT_MANUAL.md`
4. `.github/ALL_MARKDOWN.md`
5. `README.md`
6. relevant `.github/architecture/**` docs

## Non-Negotiables

- No production dependencies.
- No Spring, Quarkus, Jakarta, Lombok, YAML, TOML, JSON parser, logging framework, Bean Validation, or cloud secret manager integration in core v0.1.0.
- Do not add dependencies without explicit human approval.
- Keep public API changes small and documented.
- `SearchOrder` is read order.
- Default merge is `MergeStrategy.FILL_MISSING_ONLY`.
- `.env` files are opt-in.
- Java records are the supported mapping target in v0.1.0.
- Do not document normal Java classes as supported.
- Add or update tests for behavior changes.
- Add or update docs for behavior changes.
- Keep secret values masked in reports and exceptions.
- Run release-sensitive verification before claiming completion.

## Verification

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

Compile-scope dependencies must remain `none`.

## Code Style

- Prefer records in examples and tests.
- Keep internals under `io.github.arthurhoch.kissconfig.internal`.
- Keep errors actionable and deterministic.
- Avoid broad refactors during focused behavior changes.
- Never weaken tests to make a build pass.
