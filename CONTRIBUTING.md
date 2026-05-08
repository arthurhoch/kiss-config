# Contributing

KissConfig follows a small API, zero-production-dependency policy. Contributions should preserve Java 17 compatibility and avoid framework-specific code.

## Local Checks

Run:

```bash
mvn -B clean verify
mvn -B javadoc:javadoc
```

Do not weaken tests to make a change pass. Add or update tests and documentation whenever behavior changes.

## Dependency Policy

Production dependencies are not allowed in v0.1.0. Test dependencies are limited to JUnit Jupiter unless a maintainer explicitly approves otherwise.

## Pull Requests

- Keep public API additions small and documented with Javadoc.
- Update `CHANGELOG.md` for user-visible changes.
- Update `README.md` and `docs/` when behavior changes.
- Keep errors actionable and secret values masked.
