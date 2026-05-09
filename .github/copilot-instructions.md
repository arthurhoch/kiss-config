# Copilot Instructions

KissConfig is a zero-production-dependency Java 17 configuration library for mapping `.properties`, explicit `.env`, Java system properties, and OS environment variables into Java records.

Read `AGENTS.md`, `PROJECT_CONTEXT.md`, and `docs/AI_PROJECT_MANUAL.md` before broad changes.

Rules:

- Preserve package root `io.github.arthurhoch.kissconfig`.
- Keep production code standard-library only.
- Do not add Spring, Quarkus, Jakarta, Lombok, Bean Validation, logging frameworks, YAML, TOML, or JSON to core v0.1.0.
- Keep public API additions minimal.
- Records are the supported mapping target in v0.1.0; do not document normal classes as supported.
- `SearchOrder` is read order.
- Default merge is `MergeStrategy.FILL_MISSING_ONLY`.
- `.env` files are opt-in.
- Keep reports and exceptions from exposing secret values.
- Update docs, tests, and `CHANGELOG.md` for user-visible behavior changes.
- Write Javadocs for public API.

Before claiming release-sensitive work is complete, run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

## Versioned AI Skills

Before creating a release tag, read `.github/skills-release-policy.md` and update the versioned Markdown skill artifacts under `docs/skills/`. Add a new `docs/skills/vX.Y.Z.md` file, update `docs/skills/index.md`, keep older skill files, and verify the complete public API/member index for the release.
