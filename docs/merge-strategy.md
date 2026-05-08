# Merge Strategy

`MergeStrategy.FILL_MISSING_ONLY` is the default.

## FILL_MISSING_ONLY

Earlier sources fill values first. Later sources only add missing keys.

## OVERRIDE_EXISTING

Later sources override earlier sources. Configure this explicitly:

```java
KissConfig.builder()
        .mergeStrategy(MergeStrategy.OVERRIDE_EXISTING)
        .mapTo(AppConfig.class);
```

## FAIL_ON_DUPLICATE

If the same canonical key appears more than once, loading fails with `ConfigDuplicateKeyException`.
