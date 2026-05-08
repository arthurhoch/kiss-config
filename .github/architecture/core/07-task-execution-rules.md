# Task Execution Rules

Use this order for project-wide changes:

1. Read user-facing docs and architecture docs.
2. Implement behavior in the smallest relevant module.
3. Add or update tests.
4. Update docs and changelog.
5. Run `mvn -B clean verify`.
6. Run `mvn -B javadoc:javadoc` for public API changes.
