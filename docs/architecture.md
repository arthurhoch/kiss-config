# Architecture

Pipeline:

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

Most production code lives in internal packages. The public API stays small and focuses on builder configuration, search order, locations, reports, annotations, and exceptions.
