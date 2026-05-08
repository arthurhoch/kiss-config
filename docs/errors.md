# Errors

All KissConfig exceptions extend `ConfigException`.

Exception types:

- `ConfigLoadException`
- `ConfigParseException`
- `ConfigMappingException`
- `ConfigInterpolationException`
- `ConfigDuplicateKeyException`
- `ConfigMissingPropertyException`
- `ConfigInvalidProfileException`

Errors include source, key, target type, safe value, and actionable context when available.

Example style:

```text
Cannot map property 'server.port' to int.
Value: "abc"
Target: AppConfig.server.port
Source: /opt/app/application.properties
Expected: integer number
```
