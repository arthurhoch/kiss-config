# Search Order

`SearchOrder` is the order of reading, not priority by name.

The active merge strategy decides conflict behavior:

- `FILL_MISSING_ONLY`: earlier sources fill values first and later sources only add missing keys.
- `OVERRIDE_EXISTING`: later sources override earlier values.
- `FAIL_ON_DUPLICATE`: repeated canonical keys fail.

## Presets

`SearchOrder.defaults()`:

1. `ConfigLocation.classpathLibraries()`
2. `ConfigLocation.classpath()`
3. `ConfigLocation.jarDirectory()`
4. `ConfigLocation.workingDirectory()`
5. `ConfigLocation.systemProperties()`
6. `ConfigLocation.environmentVariables()`

`SearchOrder.externalFirst()`:

1. `ConfigLocation.environmentVariables()`
2. `ConfigLocation.systemProperties()`
3. `ConfigLocation.envFile()`
4. `ConfigLocation.workingDirectory()`
5. `ConfigLocation.jarDirectory()`
6. `ConfigLocation.classpath()`
7. `ConfigLocation.classpathLibraries()`

`externalFirst()` is designed for the default `FILL_MISSING_ONLY` mode. Because external sources are read first, they have priority. With `OVERRIDE_EXISTING`, the last source wins, so this meaning changes.

`SearchOrder.production()` uses the same order as `externalFirst()` and does not hardcode `/etc` paths.

`SearchOrder.classpathOnly()` reads classpath library defaults and application classpath files.

`SearchOrder.test()` reads working directory and application classpath.

`SearchOrder.none()` reads no sources.
