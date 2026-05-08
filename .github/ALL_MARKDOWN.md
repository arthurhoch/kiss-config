# Markdown Index

This file indexes every Markdown document in the repository, excluding generated files under `target/`. AI agents should use it to choose the smallest relevant reading set for a task.

## Recommended Reading Paths

| Task | Read These First |
|---|---|
| Any non-trivial AI task | `AGENTS.md`, `PROJECT_CONTEXT.md`, `docs/AI_PROJECT_MANUAL.md`, this file, `README.md` |
| Public API or behavior change | `README.md`, `docs/api.md`, relevant docs page, `.github/architecture/index.md`, relevant module docs |
| Search order or merge behavior | `docs/search-order.md`, `docs/merge-strategy.md`, `.github/architecture/modules/search-order.md`, `.github/architecture/modules/merge-strategy.md` |
| Env files or profiles | `docs/env-files.md`, `docs/profiles.md`, `docs/config-locations.md`, related architecture modules |
| Mapping or annotations | `docs/mapping.md`, `docs/annotations.md`, `.github/architecture/modules/mapping.md`, `.github/architecture/modules/type-conversion.md` |
| Release work | `docs/release.md`, `docs/maven-central.md`, `docs/github-pages.md`, `CHANGELOG.md`, `RELEASE_READINESS_REPORT.md`, `FINAL_RELEASE_AUDIT.md` |
| Security or secrets | `SECURITY.md`, `docs/secrets.md`, `.github/architecture/modules/secrets-and-reporting.md` |

## Root Documents

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`../README.md`](../README.md) | User-facing overview, install status, quick start, examples, limitations, docs links. | Always before user-facing docs or API changes. | User-facing | Yes |
| [`../PROJECT_CONTEXT.md`](../PROJECT_CONTEXT.md) | Short pasteable AI context for KissConfig. | Early in AI sessions. | AI-facing | Yes |
| [`../AGENTS.md`](../AGENTS.md) | Required AI agent rules, read order, guardrails, command checklist. | First for AI/code-agent work. | AI-facing | Yes |
| [`../CAVEMAN.md`](../CAVEMAN.md) | Compact low-token project summary for AI agents and maintainers. | Early in AI sessions. | AI-facing | No |
| [`../CHANGELOG.md`](../CHANGELOG.md) | User-visible change history. | Any behavior, docs, release, or API change. | User-facing | Yes |
| [`../CONTRIBUTING.md`](../CONTRIBUTING.md) | Contribution process and checks. | Before contributor workflow or policy edits. | User-facing | No |
| [`../SECURITY.md`](../SECURITY.md) | Security reporting and secret-handling expectations. | Security, secrets, reports, exceptions. | User-facing | Yes |
| [`../CODE_OF_CONDUCT.md`](../CODE_OF_CONDUCT.md) | Community conduct policy. | Governance changes. | User-facing | No |
| [`../RELEASE_READINESS_REPORT.md`](../RELEASE_READINESS_REPORT.md) | Earlier release readiness audit. | Release audits and comparing readiness state. | Maintainer-facing | Yes |
| [`../FINAL_RELEASE_AUDIT.md`](../FINAL_RELEASE_AUDIT.md) | Final release-blocker audit report. | Before release prompt or release workflow. | Maintainer-facing | Yes |
| [`../GITHUB_PUSH_REPORT.md`](../GITHUB_PUSH_REPORT.md) | First GitHub push report and manual next steps. | Repository setup and push history. | Maintainer-facing | Yes |
| [`../DOCUMENTATION_UPGRADE_REPORT.md`](../DOCUMENTATION_UPGRADE_REPORT.md) | Documentation upgrade summary and verification results. | Before release docs review. | Maintainer-facing | Yes |
| [`../DOCS_IMPLEMENTATION_AUDIT.md`](../DOCS_IMPLEMENTATION_AUDIT.md) | Documentation-to-implementation audit and verification results. | Before release documentation signoff. | Maintainer-facing | Yes |
| [`../RELEASE_0_1_0_REPORT.md`](../RELEASE_0_1_0_REPORT.md) | Release 0.1.0 workflow, tag, GitHub Release, Central Portal, and next-version report. | After or during release verification. | Maintainer-facing | Yes |

## User Documentation

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`../docs/index.md`](../docs/index.md) | GitHub Pages landing page. | Docs site changes. | User-facing | Yes |
| [`../docs/ai-usage.md`](../docs/ai-usage.md) | User-facing AI usage guidance for consumer projects. | AI usage docs and examples. | User-facing | No |
| [`../docs/getting-started.md`](../docs/getting-started.md) | First-use guide and basic builder usage. | Onboarding docs. | User-facing | Yes |
| [`../docs/api.md`](../docs/api.md) | Compact public API reference. | Public API changes. | User-facing | Yes |
| [`../docs/search-order.md`](../docs/search-order.md) | Read-order semantics, presets, `externalFirst()` caveats. | Source priority or search order work. | User-facing | Yes |
| [`../docs/config-locations.md`](../docs/config-locations.md) | `ConfigLocation` factories and file resolution. | Source resolution work. | User-facing | Yes |
| [`../docs/merge-strategy.md`](../docs/merge-strategy.md) | Conflict behavior and source metadata consequences. | Merge behavior work. | User-facing | Yes |
| [`../docs/profiles.md`](../docs/profiles.md) | Profile validation and filename expansion. | Profile behavior work. | User-facing | Yes |
| [`../docs/env-files.md`](../docs/env-files.md) | `.env` opt-in behavior and parser subset. | Env parser/source work. | User-facing | Yes |
| [`../docs/properties-files.md`](../docs/properties-files.md) | `.properties` parsing and profile variants. | Properties parser work. | User-facing | Yes |
| [`../docs/interpolation.md`](../docs/interpolation.md) | `${NAME}` and `${NAME:default}` behavior. | Interpolation work. | User-facing | Yes |
| [`../docs/mapping.md`](../docs/mapping.md) | Record mapping, types, null behavior, unsupported targets. | Mapper/converter work. | User-facing | Yes |
| [`../docs/annotations.md`](../docs/annotations.md) | `@ConfigName`, `@DefaultValue`, `@Required`, `@Secret`. | Annotation or mapper work. | User-facing | Yes |
| [`../docs/security.md`](../docs/security.md) | Security model, dependency policy, and safe reporting guidance. | Security docs and policy changes. | User-facing | Yes |
| [`../docs/secrets.md`](../docs/secrets.md) | Automatic and annotation-based secret masking. | Reports, exceptions, secret detection. | User-facing | Yes |
| [`../docs/errors.md`](../docs/errors.md) | Exception model and example messages. | Error handling changes. | User-facing | Yes |
| [`../docs/reporting.md`](../docs/reporting.md) | `KissConfigResult` and report output. | Report/source metadata work. | User-facing | Yes |
| [`../docs/examples.md`](../docs/examples.md) | Complete practical examples. | README/docs examples work. | User-facing | Yes |
| [`../docs/testing.md`](../docs/testing.md) | Testing guidance and project test coverage expectations. | Test work. | User-facing | No |
| [`../docs/testing-report.md`](../docs/testing-report.md) | Current verification report and known limits. | Verification reporting. | User-facing | No |
| [`../docs/release.md`](../docs/release.md) | Manual release process and Central Portal flow. | Release work. | Maintainer-facing | Yes |
| [`../docs/maven-central.md`](../docs/maven-central.md) | Maven Central/Sonatype Central Portal setup. | Release setup. | Maintainer-facing | Yes |
| [`../docs/github-pages.md`](../docs/github-pages.md) | GitHub Pages setup and workflow expectations. | Docs publication work. | Maintainer-facing | Yes |
| [`../docs/architecture.md`](../docs/architecture.md) | User-facing architecture overview. | Architecture overview edits. | User-facing | No |
| [`../docs/roadmap.md`](../docs/roadmap.md) | Future candidate features and out-of-scope list. | Scope decisions and planning. | User-facing | Yes |
| [`../docs/AI_PROJECT_MANUAL.md`](../docs/AI_PROJECT_MANUAL.md) | Complete AI-oriented project manual. | AI sessions and broad changes. | AI-facing | Yes |

## Repository Policy

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`ALL_MARKDOWN.md`](ALL_MARKDOWN.md) | This Markdown index. | Any docs inventory work. | AI-facing | Yes |
| [`ai-rules.md`](ai-rules.md) | AI-specific project rules. | AI behavior or policy changes. | AI-facing | Yes |
| [`copilot-instructions.md`](copilot-instructions.md) | Concise Copilot guardrails. | Copilot instruction changes. | AI-facing | Yes |
| [`logging-guidelines.md`](logging-guidelines.md) | No-framework logging policy. | Diagnostics/logging decisions. | Maintainer-facing | No |
| [`profile/engineering-preferences.md`](profile/engineering-preferences.md) | Engineering style preferences. | Broad implementation style changes. | Maintainer-facing | No |

## Architecture Core

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`architecture/index.md`](architecture/index.md) | Architecture map and critical invariants. | Any architecture-related change. | AI-facing | Yes |
| [`architecture/core/00-authority-and-clarification.md`](architecture/core/00-authority-and-clarification.md) | Source-of-truth and conflict resolution rules. | Ambiguous behavior or docs/code mismatch. | AI-facing | Yes |
| [`architecture/core/01-system-purpose.md`](architecture/core/01-system-purpose.md) | Product purpose and value proposition. | Product positioning changes. | AI-facing | Yes |
| [`architecture/core/02-core-principles.md`](architecture/core/02-core-principles.md) | KISS and dependency principles. | Scope or dependency changes. | AI-facing | Yes |
| [`architecture/core/03-ai-behavior-rules.md`](architecture/core/03-ai-behavior-rules.md) | AI execution rules. | AI rule changes. | AI-facing | Yes |
| [`architecture/core/04-code-generation-rules.md`](architecture/core/04-code-generation-rules.md) | Code generation boundaries. | Implementation changes. | AI-facing | No |
| [`architecture/core/05-repository-structure.md`](architecture/core/05-repository-structure.md) | Expected repository layout. | Structure changes. | AI-facing | No |
| [`architecture/core/06-assumptions-and-defaults.md`](architecture/core/06-assumptions-and-defaults.md) | Default behavior and assumptions. | Default behavior changes. | AI-facing | Yes |
| [`architecture/core/07-task-execution-rules.md`](architecture/core/07-task-execution-rules.md) | Implementation workflow. | Process changes. | AI-facing | No |

## Architecture Modules

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`architecture/modules/config-loading.md`](architecture/modules/config-loading.md) | End-to-end loading pipeline. | Loader pipeline changes. | AI-facing | Yes |
| [`architecture/modules/search-order.md`](architecture/modules/search-order.md) | Search order semantics and `externalFirst()`. | Search order changes. | AI-facing | Yes |
| [`architecture/modules/config-location.md`](architecture/modules/config-location.md) | Location factories and source resolution. | Config location changes. | AI-facing | Yes |
| [`architecture/modules/merge-strategy.md`](architecture/modules/merge-strategy.md) | Merge strategy rules. | Merge changes. | AI-facing | Yes |
| [`architecture/modules/env-parser.md`](architecture/modules/env-parser.md) | `.env` parser behavior and opt-in rule. | Env parser changes. | AI-facing | Yes |
| [`architecture/modules/properties-parser.md`](architecture/modules/properties-parser.md) | `.properties` parser behavior. | Properties parser changes. | AI-facing | Yes |
| [`architecture/modules/interpolation.md`](architecture/modules/interpolation.md) | Interpolation rules. | Interpolation changes. | AI-facing | Yes |
| [`architecture/modules/key-normalization.md`](architecture/modules/key-normalization.md) | Canonical key normalization. | Key normalization changes. | AI-facing | Yes |
| [`architecture/modules/mapping.md`](architecture/modules/mapping.md) | Record mapping and unsupported classes. | Mapper changes. | AI-facing | Yes |
| [`architecture/modules/type-conversion.md`](architecture/modules/type-conversion.md) | Type converter support. | Converter changes. | AI-facing | Yes |
| [`architecture/modules/secrets-and-reporting.md`](architecture/modules/secrets-and-reporting.md) | Masking and reports. | Secret/report changes. | AI-facing | Yes |
| [`architecture/modules/error-model.md`](architecture/modules/error-model.md) | Exception model. | Error handling changes. | AI-facing | Yes |
| [`architecture/modules/testing-strategy.md`](architecture/modules/testing-strategy.md) | Test strategy. | Test coverage changes. | AI-facing | No |
| [`architecture/modules/dependency-policy.md`](architecture/modules/dependency-policy.md) | Zero production dependency policy. | Dependency changes. | AI-facing | Yes |

## Architecture Decisions

| Path | Purpose | When To Read | Audience | Release-Critical |
|---|---|---|---|---|
| [`architecture/decisions/0001-zero-production-dependencies.md`](architecture/decisions/0001-zero-production-dependencies.md) | Decision to keep production dependencies at zero. | Dependency decisions. | AI-facing | Yes |
| [`architecture/decisions/0002-search-order-is-read-order.md`](architecture/decisions/0002-search-order-is-read-order.md) | Search order is read order. | Search order or priority discussions. | AI-facing | Yes |
| [`architecture/decisions/0003-default-merge-fills-missing.md`](architecture/decisions/0003-default-merge-fills-missing.md) | Default merge fills missing only. | Merge default changes. | AI-facing | Yes |
| [`architecture/decisions/0004-env-files-are-explicit.md`](architecture/decisions/0004-env-files-are-explicit.md) | Env files are explicit. | Env loading changes. | AI-facing | Yes |
| [`architecture/decisions/0005-records-are-primary-target.md`](architecture/decisions/0005-records-are-primary-target.md) | Records are the supported v0.1.0 mapping target. | Mapping target changes. | AI-facing | Yes |
| [`architecture/decisions/0006-classpath-library-defaults-location.md`](architecture/decisions/0006-classpath-library-defaults-location.md) | Library defaults location. | Classpath library defaults changes. | AI-facing | Yes |
| [`architecture/decisions/0007-no-yaml-json-toml-in-core.md`](architecture/decisions/0007-no-yaml-json-toml-in-core.md) | No YAML/JSON/TOML in core. | Parser/scope changes. | AI-facing | Yes |
