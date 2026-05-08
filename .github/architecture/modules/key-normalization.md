# Key Normalization Module

Canonical keys use lower-case dot form:

- `SERVER_PORT` becomes `server.port`
- `server.port` remains `server.port`
- `server_port` becomes `server.port`
- `server-port` becomes `server.port`
- `DATABASE_POOL_SIZE` becomes `database.pool.size`

Collisions are possible when different raw keys normalize to the same canonical key.
