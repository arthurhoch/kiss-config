# Copilot Instructions

This repository builds KissConfig, a zero-production-dependency Java 17 configuration library.

Follow these rules:

- Preserve the package root `io.github.arthurhoch.kissconfig`.
- Keep public API additions minimal.
- Use Java standard library APIs only in production code.
- Use JUnit Jupiter only for tests.
- Do not introduce framework-specific code.
- Document public APIs with Javadocs.
- Update Markdown docs and `CHANGELOG.md` for user-visible behavior changes.
- Keep reports and exceptions from exposing secret values.

Before suggesting that work is complete, run:

```bash
mvn -B clean verify
```
