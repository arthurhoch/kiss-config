# Merge Strategy Module

Supported strategies:

- `FILL_MISSING_ONLY`: earlier value wins.
- `OVERRIDE_EXISTING`: later value wins.
- `FAIL_ON_DUPLICATE`: repeated canonical key fails.

The default is `FILL_MISSING_ONLY`.

`OVERRIDE_EXISTING` and `FAIL_ON_DUPLICATE` are opt-in. Source metadata and reports must point to the effective winning source after merge.
