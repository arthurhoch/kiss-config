# Logging Guidelines

KissConfig has no production logging framework dependency.

## Library Behavior

- Do not log from production code.
- Return diagnostics through `KissConfigResult.report()`.
- Throw actionable exceptions when loading, parsing, interpolation, mapping, or profile validation fails.
- Mask secret values in diagnostics.

Applications may log the generated report with their own logging framework if they choose.
