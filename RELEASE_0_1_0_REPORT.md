# Release 0.1.0 Report

## 1. Pre-Release Verification Commands And Results

Repository checks:

```bash
git status --short
git branch --show-current
git pull --ff-only origin main
```

Results:

- Working tree was clean before release preparation.
- Branch was `main`.
- Pull from `origin/main` completed with `Already up to date`.
- Initial project version was `0.1.0-SNAPSHOT`.
- Release workflow `.github/workflows/release.yml` existed and accepted workflow input `version`.
- Documentation did not claim Maven Central availability before publication.
- `CHANGELOG.md` contained 0.1.0 release notes.

Local verification before the release version change:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

Results:

- `mvn -B clean verify`: PASS, 35 tests run, 0 failures, 0 errors, 0 skipped.
- `mvn -B javadoc:javadoc`: PASS.
- `mvn -B dependency:list -DincludeScope=compile`: PASS, resolved compile dependencies: `none`.

Release-version verification:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
```

Results:

- `mvn -B clean verify`: PASS, 35 tests run, 0 failures, 0 errors, 0 skipped.
- `mvn -B javadoc:javadoc`: PASS.

Next-development verification:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
```

Results:

- `mvn -B clean verify`: PASS, 35 tests run, 0 failures, 0 errors, 0 skipped.
- `mvn -B javadoc:javadoc`: PASS.

## 2. Version Change From 0.1.0-SNAPSHOT To 0.1.0

Command:

```bash
mvn -B versions:set -DnewVersion=0.1.0 -DgenerateBackupPoms=false
```

Result: PASS.

The release commit changed the Maven project version from `0.1.0-SNAPSHOT` to `0.1.0` and marked `CHANGELOG.md` with the release date `2026-05-08`.

## 3. Release Commit Hash

```text
7b4057764634aa41cc0dd9368e4e4bfb929d090e
```

Commit message:

```text
Prepare release 0.1.0
```

## 4. Workflow Name And Run URL

Workflow:

```text
Release
```

Run URL:

```text
https://github.com/arthurhoch/kiss-config/actions/runs/25552796502
```

Workflow dispatch input:

```text
version=0.1.0
```

## 5. Workflow Status

Status:

```text
completed
```

Conclusion:

```text
success
```

Completed at:

```text
2026-05-08T11:22:15Z
```

## 6. Tag Created

Tag:

```text
v0.1.0
```

Remote tag target:

```text
7b4057764634aa41cc0dd9368e4e4bfb929d090e
```

## 7. GitHub Release Status

GitHub Release URL:

```text
https://github.com/arthurhoch/kiss-config/releases/tag/v0.1.0
```

Status:

- Draft: false
- Prerelease: false
- Published at: `2026-05-08T11:22:12Z`

## 8. Maven Central Publication Status

The GitHub Actions release workflow completed successfully, including the `Deploy to Maven Central Portal` step.

The project is configured with:

```text
autoPublish=false
```

Because of that setting, the deployment may require maintainer review and publishing in Sonatype Central Portal before it appears in Maven Central public repositories.

Repository URL checked:

```text
https://repo1.maven.org/maven2/io/github/arthurhoch/kiss-config/0.1.0/kiss-config-0.1.0.pom
```

Result at audit time:

```text
404
```

This does not by itself indicate a failed release workflow. Maven Central publication and indexing can take time, and `autoPublish=false` can require manual Central Portal publication.

## 9. Version Change To 0.1.1-SNAPSHOT

Command:

```bash
mvn -B versions:set -DnewVersion=0.1.1-SNAPSHOT -DgenerateBackupPoms=false
```

Result: PASS.

`CHANGELOG.md` now contains a new `0.1.1-SNAPSHOT` section for unreleased changes.

## 10. Next Development Commit Hash

```text
78311ad14a4520350cb8e234e244a620050fc882
```

Commit message:

```text
Prepare next development version 0.1.1-SNAPSHOT
```

Remote `main` points to:

```text
78311ad14a4520350cb8e234e244a620050fc882
```

## 11. Remaining Manual Checks

1. Open Sonatype Central Portal and confirm the `0.1.0` deployment is present.
2. If the deployment is validated but not published, manually publish it in Central Portal because `autoPublish=false`.
3. Wait for Maven Central indexing.
4. Recheck the Maven Central repository URL:

   ```text
   https://repo1.maven.org/maven2/io/github/arthurhoch/kiss-config/0.1.0/kiss-config-0.1.0.pom
   ```

5. Confirm GitHub Pages publishes docs and Javadocs successfully.
6. After Central indexing, update badges or README language only if needed.
