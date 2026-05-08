package io.github.arthurhoch.kissconfig.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a configuration value as secret so it is masked in reports and exceptions.
 *
 * <p>KissConfig also masks common secret-looking keys automatically, but this annotation should be
 * used for any sensitive record component whose key is not obvious.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.RECORD_COMPONENT, ElementType.FIELD})
public @interface Secret {
}
