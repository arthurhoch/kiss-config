# 0003 Default Merge Fills Missing

## Decision

The default merge strategy is `FILL_MISSING_ONLY`.

## Consequence

Existing values are not overridden unless users explicitly choose `OVERRIDE_EXISTING`.
