# Examples

## Custom Search Order

```java
AppConfig config = KissConfig.builder()
        .propertyFile("application.properties")
        .envFile(".env")
        .searchOrder(SearchOrder.of(
                ConfigLocation.classpath(),
                ConfigLocation.jarDirectory(),
                ConfigLocation.workingDirectory(),
                ConfigLocation.path("/etc/myapp"),
                ConfigLocation.envFile("/run/secrets/myapp.env"),
                ConfigLocation.systemProperties(),
                ConfigLocation.environmentVariables()
        ))
        .mapTo(AppConfig.class);
```

## Interpolation

```properties
server.port=${SERVER_PORT:8080}
database.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:app}
```

## Defaults

```java
public record ServerConfig(
        @DefaultValue("0.0.0.0") String host,
        @DefaultValue("8080") int port
) {}
```
