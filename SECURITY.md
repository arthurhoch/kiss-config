# Security Policy

## Supported Versions

KissConfig is pre-1.0. Security fixes are applied to the active development line until stable releases define a longer support window.

## Reporting a Vulnerability

Please report security issues privately to the maintainer before opening a public issue. Include:

- affected version or commit
- reproduction steps
- expected impact
- any safe proof-of-concept details

## Secret Handling

KissConfig masks values for keys that look secret and for values mapped through `@Secret`. Reports and exceptions should avoid exposing credentials, tokens, passwords, private keys, and similar values.
