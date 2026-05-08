# 0005 Records Are Primary Target

## Decision

Java records are the supported mapping target in v0.1.0.

## Consequence

The mapper focuses on immutable constructor binding and clear missing-value errors.

Normal JavaBeans, field-based classes, setters, and arbitrary constructors are future candidates, not v0.1.0 behavior.
