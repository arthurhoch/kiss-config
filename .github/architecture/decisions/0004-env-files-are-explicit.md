# 0004 Env Files Are Explicit

## Decision

`.env` files are loaded only when env file loading is explicitly configured.

## Consequence

Directory-based discovery does not load `.env` by surprise.
