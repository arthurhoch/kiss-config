package io.github.arthurhoch.kissconfig;

/**
 * Defines how values from later sources interact with values already loaded from earlier sources.
 */
public enum MergeStrategy {
    /**
     * Keep the first value for a canonical key. Later sources only add missing keys.
     */
    FILL_MISSING_ONLY,

    /**
     * Replace an existing value when a later source contains the same canonical key.
     */
    OVERRIDE_EXISTING,

    /**
     * Fail when a canonical key appears more than once.
     */
    FAIL_ON_DUPLICATE
}
