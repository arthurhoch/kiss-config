package io.github.arthurhoch.kissconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Changes the configuration key used for a record component or field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD})
public @interface ConfigName {
    /**
     * Returns the configured key name. Values containing a dot are treated as full keys.
     *
     * @return key name
     */
    String value();
}
