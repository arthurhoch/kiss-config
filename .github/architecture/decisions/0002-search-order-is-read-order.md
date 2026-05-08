# 0002 Search Order Is Read Order

## Decision

`SearchOrder` defines the order sources are read.

## Consequence

Priority depends on the active `MergeStrategy`. Under `FILL_MISSING_ONLY`, earlier sources win. Under `OVERRIDE_EXISTING`, later sources win.
