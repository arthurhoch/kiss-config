package io.github.arthurhoch.kissconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provides a string default value when no configuration value exists for a record component.
 *
 * <p>The default is interpolated and converted in the same way as a loaded string value.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD})
public @interface DefaultValue {
    /**
     * Returns the default string value.
     *
     * @return default value
     */
    String value();
}
