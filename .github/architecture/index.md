# Architecture Index

KissConfig loads configuration through this deterministic pipeline:

1. `SearchOrder`
2. `ConfigLocation`
3. `ConfigSourceResolver`
4. `ConfigSource`
5. Parser
6. Normalized `ConfigMap`
7. `MergeStrategy`
8. `VariableInterpolator`
9. `ConfigMapper`
10. `TypeConverter`
11. Typed config object
12. `KissConfigResult` report

Read the core documents first, then the module documents relevant to the task. Architecture decisions under `decisions/` are binding unless a maintainer explicitly changes them and updates user documentation.

## Core

- [`core/00-authority-and-clarification.md`](core/00-authority-and-clarification.md)
- [`core/01-system-purpose.md`](core/01-system-purpose.md)
- [`core/02-core-principles.md`](core/02-core-principles.md)
- [`core/03-ai-behavior-rules.md`](core/03-ai-behavior-rules.md)
- [`core/04-code-generation-rules.md`](core/04-code-generation-rules.md)
- [`core/05-repository-structure.md`](core/05-repository-structure.md)
- [`core/06-assumptions-and-defaults.md`](core/06-assumptions-and-defaults.md)
- [`core/07-task-execution-rules.md`](core/07-task-execution-rules.md)

## Modules

- [`modules/config-loading.md`](modules/config-loading.md)
- [`modules/search-order.md`](modules/search-order.md)
- [`modules/config-location.md`](modules/config-location.md)
- [`modules/merge-strategy.md`](modules/merge-strategy.md)
- [`modules/env-parser.md`](modules/env-parser.md)
- [`modules/properties-parser.md`](modules/properties-parser.md)
- [`modules/interpolation.md`](modules/interpolation.md)
- [`modules/key-normalization.md`](modules/key-normalization.md)
- [`modules/mapping.md`](modules/mapping.md)
- [`modules/type-conversion.md`](modules/type-conversion.md)
- [`modules/secrets-and-reporting.md`](modules/secrets-and-reporting.md)
- [`modules/error-model.md`](modules/error-model.md)
- [`modules/testing-strategy.md`](modules/testing-strategy.md)
- [`modules/dependency-policy.md`](modules/dependency-policy.md)
