# Search Order Module

`SearchOrder` is read order. It is not priority by name.

With the default `FILL_MISSING_ONLY` strategy, earlier sources have priority because they fill values first. With `OVERRIDE_EXISTING`, later sources win. With `FAIL_ON_DUPLICATE`, repeated canonical keys fail.

`externalFirst()` and `production()` read environment variables and system properties first to favor externally supplied values under the default merge strategy.

`externalFirst()` must always be documented with the merge-strategy caveat:

| Merge Strategy | Source order A -> B | Effective value when both define same key |
|---|---|---|
| `FILL_MISSING_ONLY` | A then B | A |
| `OVERRIDE_EXISTING` | A then B | B |
| `FAIL_ON_DUPLICATE` | A then B | error |

`SearchOrder.production()` does not enable profile `prod`, does not hardcode `/etc`, and does not load `.env` unless env file loading is configured.
