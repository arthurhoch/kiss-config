# API

Primary public API:

- `KissConfig.load(Class<T>)`
- `KissConfig.builder()`
- `KissConfigBuilder`
- `KissConfigResult<T>`
- `SearchOrder`
- `ConfigLocation`
- `ConfigLocationType`
- `MergeStrategy`
- `ConfigMap`
- `ConfigValue`
- `LoadedConfigSource`
- exception types under `io.github.arthurhoch.kissconfig.exceptions`
- annotations under `io.github.arthurhoch.kissconfig.annotations`

The builder supports configuring the property file name, env file name, single profile, search order, merge strategy, and explicit-file missing behavior.
