# Release

Releases are manual through `.github/workflows/release.yml`.

Required GitHub secrets:

- `MAVEN_CENTRAL_USERNAME`
- `MAVEN_CENTRAL_PASSWORD`
- `GPG_PRIVATE_KEY`
- `GPG_PASSPHRASE`

The Maven Central namespace `io.github.arthurhoch` must be configured and verified in Sonatype Central Portal before the first release.

## Workflow

1. Ensure `main` is green.
2. Run the `Release` workflow manually.
3. Provide a non-SNAPSHOT version such as `0.1.0`.
4. The workflow updates the Maven project version, runs tests, signs artifacts, deploys through the Central Portal Maven plugin, tags `v{version}`, and creates a GitHub Release.

The Central Portal plugin is configured with `autoPublish=false`, so maintainers should review and publish the validated deployment in the Central Portal unless the release policy is changed.
