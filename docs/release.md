# Release

KissConfig releases are manual through `.github/workflows/release.yml`.

Do not create tags, GitHub releases, or Maven Central publications outside the intentional release process.

## Current State

- Current development version after the first release: `0.1.1-SNAPSHOT`
- Latest GitHub release: `0.1.0`
- Maven Central status: release workflow completed, but public availability is not claimed until Central Portal publication and indexing are confirmed
- Publishing path: Sonatype Central Portal through `org.sonatype.central:central-publishing-maven-plugin`

## Before Future Releases Or Central Publication Completion

1. Push the repository to `https://github.com/arthurhoch/kiss-config`.
2. Confirm CI passes on `main`.
3. In GitHub repository settings, enable GitHub Pages with GitHub Actions as the source.
4. Create or access a Sonatype Central Portal account.
5. Configure and verify the namespace `io.github.arthurhoch` in Central Portal.
6. Generate a Central Portal user token.
7. Create or choose a GPG signing key.
8. Add GitHub Actions secrets:
   - `MAVEN_CENTRAL_USERNAME`
   - `MAVEN_CENTRAL_PASSWORD`
   - `GPG_PRIVATE_KEY`
   - `GPG_PASSPHRASE`

## Local Preflight

Run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

The compile-scope dependency list must be `none`.

Check:

- README examples still match public API.
- Docs do not claim Maven Central availability before publication.
- `CHANGELOG.md` contains 0.1.0 notes.
- `pom.xml` metadata is complete.
- Git worktree contains no generated `target/` files or private local files.

## Release Workflow

Use GitHub Actions:

1. Open the `Release` workflow.
2. Run `workflow_dispatch`.
3. Enter a non-SNAPSHOT version such as `0.1.0`.

The workflow:

1. validates that the version is not a SNAPSHOT
2. checks out the repository
3. configures JDK 17, Maven Central credentials, and GPG signing
4. sets the Maven project version to the requested release version
5. runs `mvn -B clean verify`
6. deploys signed artifacts through the Central Portal Maven plugin
7. commits the release version
8. creates tag `v{version}`
9. creates a GitHub Release

The Central Portal plugin is configured with `autoPublish=false`. Maintainers should review the deployment in Central Portal and publish it there unless project policy changes.

## After The Release

After `0.1.0` is published and validated:

1. Wait for Maven Central indexing.
2. Confirm the artifact page is visible in Central Portal and Maven Central search.
3. Update README installation language if needed.
4. Change `main` to the next development version, such as `0.1.1-SNAPSHOT`.
5. Add a new changelog section for the next development cycle.
6. Confirm Pages published docs and Javadocs.

## GitHub Pages

The Pages workflow publishes Markdown files from `docs/` and generated Javadocs under `/javadocs/`.

Manual setup is required once in repository settings:

1. Open Settings.
2. Open Pages.
3. Select GitHub Actions as the source.
4. Push to `main` or run the Pages workflow manually.

## Legacy OSSRH Note

This project uses Central Portal-oriented setup. Do not follow old OSSRH-only instructions unless a maintainer explicitly creates a legacy release path and documents why.
