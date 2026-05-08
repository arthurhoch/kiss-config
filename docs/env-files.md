# Env Files

Env files are explicit. KissConfig does not load `.env` unless `.envFile(...)` or an explicit env file location is configured.

Supported syntax:

```dotenv
# comment
VAR1=teste
SERVER_PORT=8080
DATABASE_URL=jdbc:postgresql://localhost:5432/app
export OPTIONAL=value
QUOTED="hello\nworld"
SINGLE='literal value'
```

Supported double-quoted escapes:

- `\n`
- `\t`
- `\r`
- `\"`
- `\\`
- `\=`

KissConfig does not execute shell commands and does not support complex shell interpolation.

Operating-system environment variables are loaded from `System.getenv()` and normalized, so `SERVER_PORT` becomes `server.port`.
