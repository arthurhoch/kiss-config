# Reporting

Use `KissConfigResult<T>` when you need diagnostics:

```java
KissConfigResult<AppConfig> result = KissConfig.builder()
        .searchOrder(SearchOrder.production())
        .load(AppConfig.class);

AppConfig config = result.value();
String report = result.report();
```

Reports show:

- attempted sources
- loaded sources
- skipped missing optional sources
- effective values
- source of each effective value
- masked secrets
- merge strategy
- profile
- property file
- env file
- search order
