package io.github.arthurhoch.kissconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a configuration value as required even when the target type is nullable.
 *
 * <p>Missing primitive record components already fail. Use this annotation for object, wrapper,
 * or nested record components that must be present.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD})
public @interface Required {
}
