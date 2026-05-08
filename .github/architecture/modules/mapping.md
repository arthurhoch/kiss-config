# Mapping Module

Java records are the supported target in v0.1.0. Nested records map from dot-prefixed keys. Missing primitives fail. Missing object or wrapper values map to `null` unless `@Required` is present.

`@DefaultValue` applies when no config value exists and the default value still goes through interpolation and type conversion.

Normal JavaBeans, field-based classes, setters, and arbitrary constructors are not supported in v0.1.0. Do not document them as supported unless implementation and tests are added.
