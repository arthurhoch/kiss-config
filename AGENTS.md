# AI Agent Instructions

AI agents working in this repository must:

- Read `README.md` first.
- Read `.github/ALL_MARKDOWN.md`.
- Read `.github/architecture/index.md`.
- Follow the zero-production-dependency policy.
- Do not add dependencies without explicit human approval.
- Keep the public API small.
- Update docs when behavior changes.
- Update tests when behavior changes.
- Update `CHANGELOG.md` for user-visible changes.
- Run `mvn -B clean verify` before claiming done.
- Never weaken tests to make the build pass.
- Never introduce framework-specific code.
- Never add Spring, Quarkus, Jakarta, or Lombok.
- Preserve Java 17 compatibility.
- Prefer records in examples.
- Write Javadocs for public API.
- Keep exceptions actionable.
- Keep secret values masked.

The project intentionally has no YAML, TOML, JSON, hot reload, cloud secret manager, Bean Validation, logging framework, i18n, or framework integration in v0.1.0.
