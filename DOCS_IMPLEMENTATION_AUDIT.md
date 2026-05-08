# Docs Implementation Audit

## PASS/FAIL

PASS.

The documentation is aligned with the current implementation for the audited release-readiness areas:

- README examples use real public API.
- Docs examples use real public API.
- Normal Java class mapping is documented as unsupported in v0.1.0.
- `SearchOrder` is documented as read order.
- Default merge strategy is documented as `FILL_MISSING_ONLY`.
- `.env` files are documented as opt-in.
- `externalFirst()` is documented with both `FILL_MISSING_ONLY` and `OVERRIDE_EXISTING` implications.
- Profile file expansion is documented consistently.
- Env file path behavior is documented consistently after the fixes below.
- `/opt/app` production file order is documented consistently.
- Secret masking is documented consistently with tests.
- Docs do not claim Maven Central publication before release.
- `PROJECT_CONTEXT.md` is sufficient for a new AI session.
- `docs/AI_PROJECT_MANUAL.md` is sufficient for Codex to understand the whole project.

## Inconsistencies Found

1. Env file path documentation was incomplete.
   - The docs clearly explained `ConfigLocation.envFile(path)` as one exact env file.
   - They did not clearly distinguish that builder `.envFile(path)` can also be an absolute path and, when used by directory locations, still receives profile expansion.

2. `docs/architecture.md` referenced an `internal.merge` package that does not exist.
   - Merge behavior currently lives inside `internal.source` loading.

No implementation bugs were found during the documentation audit.

## Fixes Made

- Updated `README.md` to distinguish:
  - `ConfigLocation.envFile(path)`: exact explicit env file, no automatic profile variant.
  - builder `.envFile(path)`: simple or absolute path, used by directory locations, with profile expansion.
- Updated `PROJECT_CONTEXT.md` with the same env file path distinction.
- Updated `docs/env-files.md` with builder absolute path behavior and profile expansion example.
- Updated `docs/config-locations.md` with builder env path behavior.
- Updated `docs/AI_PROJECT_MANUAL.md` with the same distinction.
- Updated `docs/api.md` to state builder `envFile(String|Path)` accepts simple names and absolute paths.
- Updated `docs/architecture.md` to remove the nonexistent `internal.merge` package reference.

## Commands Run

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
javac --release 17 -cp target/classes /tmp/kissconfig-docs-check.*/DocsExamples.java
```

## Results

- `mvn -B clean verify`: PASS
  - Tests run: 35
  - Failures: 0
  - Errors: 0
  - Skipped: 0
- `mvn -B javadoc:javadoc`: PASS
- Representative README/docs API compile check: PASS

## Remaining Risks

- Maven Central publication is still a manual/release-workflow outcome and should not be claimed until the workflow succeeds and Central indexes the artifact.
- GitHub Pages publication depends on repository settings and the Pages workflow.
- Many Markdown code blocks are illustrative snippets rather than complete standalone Java files; representative public API snippets were compiled successfully.

## Release Safety

The documentation is safe for release from an implementation-correctness standpoint.
