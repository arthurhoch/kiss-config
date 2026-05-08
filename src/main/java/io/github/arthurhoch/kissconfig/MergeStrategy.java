package io.github.arthurhoch.kissconfig;

/**
 * Defines how values from later sources interact with values already loaded from earlier sources.
 *
 * <p>{@link SearchOrder} is read order. With {@link #FILL_MISSING_ONLY}, the first source wins.
 * With {@link #OVERRIDE_EXISTING}, the last source wins. With {@link #FAIL_ON_DUPLICATE},
 * duplicate canonical keys fail. See {@code docs/merge-strategy.md} for examples.</p>
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
