# Security

KissConfig is designed to load configuration predictably while avoiding accidental secret exposure.

## Secret Handling

- Values mapped through `@Secret` are masked in reports and exceptions.
- Keys that look secret, such as `password`, `token`, `credential`, `api.key`, and `private.key`, are masked automatically.
- Do not log reports or exception details in production without reviewing application policy.

## Dependency Policy

Production code has zero external dependencies. JUnit and Maven plugins are test or build-time tooling only.

## Source Safety

`.env` files are opt-in and are not loaded automatically. KissConfig does not execute shell commands from `.env` files.

## Reporting

Report vulnerabilities privately to the maintainer before opening a public issue. See the repository [security policy](https://github.com/arthurhoch/kiss-config/blob/main/SECURITY.md).

## Quality And Coverage

Normal CI runs the fast Maven build:

```bash
mvn -B clean verify
```

JaCoCo coverage is generated during `verify`:

```text
target/site/jacoco/jacoco.xml
target/site/jacoco/index.html
```

Optional local security and static-analysis checks are:

```bash
mvn -Pdependency-check verify
mvn -Pspotbugs verify
```

Use [code-cleanup.md](code-cleanup.md) before deleting code, especially public API, secret-masking behavior, source ordering, merge logic, or mapper behavior.
