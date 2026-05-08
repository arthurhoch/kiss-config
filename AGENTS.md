# AI Agent Instructions

These instructions apply to Codex, Copilot, Cursor, ChatGPT, and any other AI agent working in this repository.

## Required Read Order

Before non-trivial work, read:

1. `AGENTS.md`
2. `PROJECT_CONTEXT.md`
3. `docs/AI_PROJECT_MANUAL.md`
4. `.github/ALL_MARKDOWN.md`
5. `README.md`
6. relevant `.github/architecture/**` docs for the task

Use `docs/AI_PROJECT_MANUAL.md` as the canonical single-file project manual for project-wide context.

## Project Guardrails

- Java 17+ only.
- Package root is `io.github.arthurhoch.kissconfig`.
- Maven coordinates are `io.github.arthurhoch:kiss-config`.
- Production code must have zero external dependencies.
- Test dependencies are limited to JUnit Jupiter unless a human explicitly approves otherwise.
- Keep the public API small.
- Java records are the supported mapping target in v0.1.0.
- Normal JavaBeans/field classes are not supported in v0.1.0.
- Preserve deterministic behavior.
- Keep exceptions actionable.
- Keep secret values masked in reports and exceptions.

## Forbidden Actions

Do not:

- add production dependencies without explicit human approval
- add Spring, Quarkus, Jakarta, Lombok, Bean Validation, logging framework, YAML, TOML, or JSON support to core v0.1.0
- introduce framework-specific code
- weaken tests to make a build pass
- remove documentation to avoid fixing inconsistency
- leak secrets in reports, exceptions, examples, logs, or generated reports
- claim Maven Central publication before the artifact is actually published and indexed
- publish to Maven Central
- create Git tags
- create GitHub releases
- change the current development `-SNAPSHOT` version to a release version outside the intentional release workflow

## Coding Rules

- Prefer existing package structure and implementation style.
- Keep internals under `io.github.arthurhoch.kissconfig.internal`.
- Do not expose internal helpers as public API unless a maintainer explicitly asks.
- Use standard library APIs in production code.
- Use `java.util.Properties` for `.properties` behavior.
- Keep `.env` parsing small and non-executing.
- Do not add runtime reflection frameworks.
- Write Javadocs for public classes and public methods.

## Documentation Rules

- Update docs when behavior changes.
- Update `README.md` examples when public API changes.
- Update `docs/AI_PROJECT_MANUAL.md` and `PROJECT_CONTEXT.md` for project-wide behavior changes.
- Update `.github/ALL_MARKDOWN.md` when Markdown files are added, removed, or repurposed.
- Update relevant architecture docs when architectural decisions change.
- Update `CHANGELOG.md` for user-visible changes.
- Do not document fake features.
- Do not show normal Java class mapping as supported unless implementation and tests prove it.
- Keep `.env` opt-in behavior explicit everywhere.
- Keep `SearchOrder` documented as read order everywhere.
- Keep default merge documented as `MergeStrategy.FILL_MISSING_ONLY`.

## Test Rules

- Add or update tests when behavior changes.
- Do not depend on the real machine environment in tests.
- Use deterministic system property and environment maps through existing test hooks.
- Cover secret masking for reports and exceptions when touching secrets.
- Cover docs-exposed behavior where practical.

## Release Rules

- Keep the current development version on a `-SNAPSHOT` suffix until the explicit release workflow.
- Maven Central publishing is Central Portal-oriented.
- Required GitHub secrets:
  - `MAVEN_CENTRAL_USERNAME`
  - `MAVEN_CENTRAL_PASSWORD`
  - `GPG_PRIVATE_KEY`
  - `GPG_PASSPHRASE`
- The Central Portal namespace `io.github.arthurhoch` must be configured and verified before release.
- GitHub Pages must be enabled from GitHub Actions before docs publication is expected.

## Command Checklist

Before claiming release readiness or completion of code/public-doc work, run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

Expected compile-scope dependency result:

```text
none
```

For purely small docs edits, still run the full checklist when the task is release-oriented.
