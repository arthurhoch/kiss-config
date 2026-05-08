# Markdown Index

This index summarizes every Markdown document in the repository and helps maintainers and AI agents choose what to read.

## Start Here

- [`README.md`](../README.md): user-facing overview, install snippet, quick start, examples, and links.
- [`AGENTS.md`](../AGENTS.md): required AI-agent behavior and project guardrails.
- [`.github/architecture/index.md`](architecture/index.md): architecture map and authoritative design links.
- [`CONTRIBUTING.md`](../CONTRIBUTING.md): local checks, dependency policy, and pull request expectations.
- [`CHANGELOG.md`](../CHANGELOG.md): user-visible changes.

## User Documentation

- [`docs/index.md`](../docs/index.md): GitHub Pages landing page.
- [`docs/getting-started.md`](../docs/getting-started.md): first load and basic Maven usage.
- [`docs/api.md`](../docs/api.md): public API summary.
- [`docs/search-order.md`](../docs/search-order.md): search order semantics and presets.
- [`docs/config-locations.md`](../docs/config-locations.md): supported config locations and file discovery.
- [`docs/merge-strategy.md`](../docs/merge-strategy.md): conflict behavior.
- [`docs/profiles.md`](../docs/profiles.md): profile validation and profile file names.
- [`docs/env-files.md`](../docs/env-files.md): `.env` parser rules.
- [`docs/properties-files.md`](../docs/properties-files.md): `.properties` parser rules.
- [`docs/interpolation.md`](../docs/interpolation.md): `${NAME}` and `${NAME:default}` behavior.
- [`docs/mapping.md`](../docs/mapping.md): record mapping and type conversion.
- [`docs/annotations.md`](../docs/annotations.md): `@ConfigName`, `@DefaultValue`, `@Required`, and `@Secret`.
- [`docs/secrets.md`](../docs/secrets.md): secret masking rules.
- [`docs/errors.md`](../docs/errors.md): exception model and diagnostic expectations.
- [`docs/reporting.md`](../docs/reporting.md): result reports.
- [`docs/examples.md`](../docs/examples.md): practical examples.
- [`docs/testing.md`](../docs/testing.md): test strategy for users.
- [`docs/release.md`](../docs/release.md): release workflow.
- [`docs/maven-central.md`](../docs/maven-central.md): Maven Central setup.
- [`docs/github-pages.md`](../docs/github-pages.md): Pages publishing.
- [`docs/architecture.md`](../docs/architecture.md): user-facing architecture overview.
- [`docs/roadmap.md`](../docs/roadmap.md): planned future scope.

## Repository Policy

- [`.github/ai-rules.md`](ai-rules.md): AI rules.
- [`.github/copilot-instructions.md`](copilot-instructions.md): Copilot-specific guardrails.
- [`.github/logging-guidelines.md`](logging-guidelines.md): no-framework logging policy.
- [`.github/profile/engineering-preferences.md`](profile/engineering-preferences.md): engineering preferences.
- [`SECURITY.md`](../SECURITY.md): vulnerability reporting and secret handling.
- [`CODE_OF_CONDUCT.md`](../CODE_OF_CONDUCT.md): project conduct standards.

## Architecture Core

- [`.github/architecture/core/00-authority-and-clarification.md`](architecture/core/00-authority-and-clarification.md): source of truth and assumptions.
- [`.github/architecture/core/01-system-purpose.md`](architecture/core/01-system-purpose.md): product purpose.
- [`.github/architecture/core/02-core-principles.md`](architecture/core/02-core-principles.md): KISS principles.
- [`.github/architecture/core/03-ai-behavior-rules.md`](architecture/core/03-ai-behavior-rules.md): AI execution rules.
- [`.github/architecture/core/04-code-generation-rules.md`](architecture/core/04-code-generation-rules.md): code generation boundaries.
- [`.github/architecture/core/05-repository-structure.md`](architecture/core/05-repository-structure.md): expected layout.
- [`.github/architecture/core/06-assumptions-and-defaults.md`](architecture/core/06-assumptions-and-defaults.md): documented defaults.
- [`.github/architecture/core/07-task-execution-rules.md`](architecture/core/07-task-execution-rules.md): implementation workflow.

## Architecture Modules

- [`.github/architecture/modules/config-loading.md`](architecture/modules/config-loading.md): pipeline.
- [`.github/architecture/modules/search-order.md`](architecture/modules/search-order.md): search order.
- [`.github/architecture/modules/config-location.md`](architecture/modules/config-location.md): locations.
- [`.github/architecture/modules/merge-strategy.md`](architecture/modules/merge-strategy.md): merging.
- [`.github/architecture/modules/env-parser.md`](architecture/modules/env-parser.md): `.env` parsing.
- [`.github/architecture/modules/properties-parser.md`](architecture/modules/properties-parser.md): `.properties` parsing.
- [`.github/architecture/modules/interpolation.md`](architecture/modules/interpolation.md): interpolation.
- [`.github/architecture/modules/key-normalization.md`](architecture/modules/key-normalization.md): key normalization.
- [`.github/architecture/modules/mapping.md`](architecture/modules/mapping.md): object mapping.
- [`.github/architecture/modules/type-conversion.md`](architecture/modules/type-conversion.md): converters.
- [`.github/architecture/modules/secrets-and-reporting.md`](architecture/modules/secrets-and-reporting.md): masking and reports.
- [`.github/architecture/modules/error-model.md`](architecture/modules/error-model.md): exceptions.
- [`.github/architecture/modules/testing-strategy.md`](architecture/modules/testing-strategy.md): tests.
- [`.github/architecture/modules/dependency-policy.md`](architecture/modules/dependency-policy.md): dependency constraints.

## Decisions

- [`.github/architecture/decisions/0001-zero-production-dependencies.md`](architecture/decisions/0001-zero-production-dependencies.md)
- [`.github/architecture/decisions/0002-search-order-is-read-order.md`](architecture/decisions/0002-search-order-is-read-order.md)
- [`.github/architecture/decisions/0003-default-merge-fills-missing.md`](architecture/decisions/0003-default-merge-fills-missing.md)
- [`.github/architecture/decisions/0004-env-files-are-explicit.md`](architecture/decisions/0004-env-files-are-explicit.md)
- [`.github/architecture/decisions/0005-records-are-primary-target.md`](architecture/decisions/0005-records-are-primary-target.md)
- [`.github/architecture/decisions/0006-classpath-library-defaults-location.md`](architecture/decisions/0006-classpath-library-defaults-location.md)
- [`.github/architecture/decisions/0007-no-yaml-json-toml-in-core.md`](architecture/decisions/0007-no-yaml-json-toml-in-core.md)
