# Code Generation Rules

- Generate Java 17-compatible code.
- Keep public classes documented with Javadoc.
- Keep implementation internals under `internal` packages.
- Prefer simple immutable value objects.
- Prefer standard library APIs over custom parsing when reliable, such as `java.util.Properties` for `.properties`.
