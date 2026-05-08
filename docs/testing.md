# Testing

Use `SearchOrder.of(ConfigLocation.path(tempDir))` for deterministic file-based tests.

Avoid depending on the real machine environment. KissConfig internals abstract system properties and environment variables so the project test suite can provide deterministic maps.

For application tests, prefer small records that represent only the configuration needed by the test.
