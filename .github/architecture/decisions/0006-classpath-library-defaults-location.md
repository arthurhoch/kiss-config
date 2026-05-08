# 0006 Classpath Library Defaults Location

## Decision

Libraries contribute defaults only through `META-INF/kiss-config/defaults.properties`.

## Consequence

KissConfig does not scan every dependency JAR for arbitrary `application.properties` files.
