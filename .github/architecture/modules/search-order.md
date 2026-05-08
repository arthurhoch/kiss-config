# Search Order Module

`SearchOrder` is read order. It is not priority by name.

With the default `FILL_MISSING_ONLY` strategy, earlier sources have priority because they fill values first. With `OVERRIDE_EXISTING`, later sources win. With `FAIL_ON_DUPLICATE`, repeated canonical keys fail.

`externalFirst()` and `production()` read environment variables and system properties first to favor externally supplied values under the default merge strategy.
