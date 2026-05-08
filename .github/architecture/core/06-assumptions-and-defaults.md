# Assumptions And Defaults

- Default property file is `application.properties`.
- Default env file is none.
- Default merge strategy is `FILL_MISSING_ONLY`.
- Default search order is classpath library defaults, application classpath, jar directory, working directory, system properties, environment variables.
- `SearchOrder` is read order, not priority by name.
- With default `FILL_MISSING_ONLY`, earlier sources win.
- With `OVERRIDE_EXISTING`, later sources win.
- Missing auto-discovered files are skipped.
- Missing explicit files fail unless `ignoreMissingExplicitFiles(true)` is configured.
- One profile is supported in v0.1.0.
- Profiles are never enabled automatically.
- `SearchOrder.production()` does not imply `profile("prod")`.
- `.env` files are not loaded unless `.envFile(...)` or `ConfigLocation.envFile(...)` is configured.
