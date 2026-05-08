# Properties Files

KissConfig parses `.properties` files with `java.util.Properties` through a UTF-8 reader.

Supported behavior includes standard comments, `key=value`, `key:value`, and whitespace separators according to `Properties` behavior.

Parse errors are wrapped in `ConfigParseException` with source metadata.
