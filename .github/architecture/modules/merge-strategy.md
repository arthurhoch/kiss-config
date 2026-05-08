# Merge Strategy Module

Supported strategies:

- `FILL_MISSING_ONLY`: earlier value wins.
- `OVERRIDE_EXISTING`: later value wins.
- `FAIL_ON_DUPLICATE`: repeated canonical key fails.

The default is `FILL_MISSING_ONLY`.
