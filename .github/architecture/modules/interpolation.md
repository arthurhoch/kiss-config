# Interpolation Module

Supported expressions:

- `${NAME}`
- `${NAME:default}`

Variables resolve against the merged canonical config map and raw source keys where useful. Missing required variables throw `ConfigInterpolationException`. Cycles are detected and reported with a dependency chain.
