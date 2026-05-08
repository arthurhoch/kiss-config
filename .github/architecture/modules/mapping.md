# Mapping Module

Java records are the primary target. Nested records map from dot-prefixed keys. Missing primitives fail. Missing object or wrapper values map to `null` unless `@Required` is present.

`@DefaultValue` applies when no config value exists and the default value still goes through interpolation and type conversion.
