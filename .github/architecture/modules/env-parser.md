# Env Parser Module

The `.env` parser supports empty lines, comments beginning with `#`, `KEY=value`, single-quoted values, double-quoted values, basic escapes in double quotes, and optional `export KEY=value`.

It does not execute shell commands and does not implement shell interpolation.

`.env` files are opt-in. Directory locations must not discover env files unless builder `.envFile(...)` is configured. Explicit `ConfigLocation.envFile(...)` loads exactly the configured env path.
