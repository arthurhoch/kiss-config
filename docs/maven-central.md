# Maven Central

KissConfig is prepared for Sonatype Central Portal publishing.

The artifact must not be described as published until release `0.1.0` has been deployed, reviewed, published in Central Portal, and indexed.

## Coordinates

```text
io.github.arthurhoch:kiss-config
```

First release target:

```text
0.1.0
```

Current development version:

```text
0.1.0-SNAPSHOT
```

## Required Manual Setup

Before the first release:

1. Push the repository to `https://github.com/arthurhoch/kiss-config`.
2. Confirm GitHub Actions CI is green on `main`.
3. Create or use a Sonatype Central Portal account.
4. Configure and verify the namespace `io.github.arthurhoch`.
5. Generate a Central Portal user token.
6. Store the token username as GitHub secret `MAVEN_CENTRAL_USERNAME`.
7. Store the token password as GitHub secret `MAVEN_CENTRAL_PASSWORD`.
8. Create or choose a GPG signing key.
9. Store the armored private key as GitHub secret `GPG_PRIVATE_KEY`.
10. Store the key passphrase as GitHub secret `GPG_PASSPHRASE`.

The namespace verification step is mandatory. The release workflow cannot publish under `io.github.arthurhoch` until Central Portal accepts the namespace.

## Maven Configuration

`pom.xml` includes:

- Apache-2.0 license metadata
- developer metadata
- SCM metadata
- issue management metadata
- source jar generation
- Javadoc jar generation
- GPG signing under the `release` profile
- Central Portal Maven plugin
- Java 17 release configuration
- zero production dependencies

The release path uses:

```text
org.sonatype.central:central-publishing-maven-plugin
```

This is Central Portal-oriented. Legacy OSSRH-only language is intentionally avoided.

## Local Checks

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
mvn -B dependency:list -DincludeScope=compile
```

Expected compile dependency output:

```text
none
```

JUnit Jupiter is allowed for tests. Production code must remain standard-library only.

## Release Flow

1. Keep `main` on `0.1.0-SNAPSHOT` until the release workflow.
2. Run the manual GitHub Actions release workflow with `0.1.0`.
3. The workflow sets the Maven version, verifies, signs, deploys, tags, and creates a GitHub Release.
4. Review and publish the deployment in Central Portal if `autoPublish=false`.
5. Wait for Maven Central indexing.
6. After validation, update `main` to the next development version, such as `0.1.1-SNAPSHOT`.

## Indexing Delay

Maven Central search and badge updates are not instant. After Central Portal publication, allow time for indexing before updating docs to claim broad availability.
