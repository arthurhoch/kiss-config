# Maven Central

KissConfig is prepared for Sonatype Central Portal publishing.

Manual setup required before release:

1. Create or use a Sonatype Central Portal account.
2. Configure and verify the namespace `io.github.arthurhoch`.
3. Generate a Central Portal user token.
4. Store the token username in `MAVEN_CENTRAL_USERNAME`.
5. Store the token password in `MAVEN_CENTRAL_PASSWORD`.
6. Create a GPG signing key.
7. Store the armored private key in `GPG_PRIVATE_KEY`.
8. Store the passphrase in `GPG_PASSPHRASE`.

The release configuration uses `org.sonatype.central:central-publishing-maven-plugin` and Central Portal language. Legacy OSSRH-only instructions are not used.

For local dry checks:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
```
