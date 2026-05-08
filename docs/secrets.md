# Secrets

KissConfig masks secret values in reports and exceptions where possible.

Values are secret when:

- a mapped component has `@Secret`
- the canonical key contains secret-looking segments such as `password`, `passwd`, `secret`, `token`, or `credential`
- the canonical key is or ends with `api.key` or `private.key`

Masked values are rendered as `******`.
