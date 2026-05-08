# Config Loading Module

The loader resolves sources from the configured `SearchOrder`, parses each source, normalizes keys, merges values, interpolates variables, maps to the target type, and returns `KissConfigResult`.

Interpolation happens after all sources are merged so values can reference keys loaded earlier or later in the configured order.
