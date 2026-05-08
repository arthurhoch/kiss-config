package io.github.arthurhoch.kissconfig;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Immutable read order for configuration locations.
 *
 * <p>Search order is read order. With the default {@link MergeStrategy#FILL_MISSING_ONLY},
 * earlier sources have priority because they fill values first. With
 * {@link MergeStrategy#OVERRIDE_EXISTING}, later sources win.</p>
 */
public final class SearchOrder implements Iterable<ConfigLocation> {
    private final List<ConfigLocation> locations;

    private SearchOrder(List<ConfigLocation> locations) {
        this.locations = List.copyOf(locations);
    }

    /**
     * Creates a search order from the supplied locations.
     *
     * @param locations locations in read order
     * @return search order
     */
    public static SearchOrder of(ConfigLocation... locations) {
        Objects.requireNonNull(locations, "locations");
        return new SearchOrder(Arrays.asList(locations));
    }

    /**
     * Returns the default read order.
     *
     * @return default search order
     */
    public static SearchOrder defaults() {
        return of(
                ConfigLocation.classpathLibraries(),
                ConfigLocation.classpath(),
                ConfigLocation.jarDirectory(),
                ConfigLocation.workingDirectory(),
                ConfigLocation.systemProperties(),
                ConfigLocation.environmentVariables()
        );
    }

    /**
     * Returns an external-first order designed for the default FILL_MISSING_ONLY merge strategy.
     *
     * <p>Because search order is read order, external sources are read first and therefore have
     * priority under {@link MergeStrategy#FILL_MISSING_ONLY}. With
     * {@link MergeStrategy#OVERRIDE_EXISTING}, the last source wins and this interpretation changes.</p>
     *
     * @return external-first search order
     */
    public static SearchOrder externalFirst() {
        return of(
                ConfigLocation.environmentVariables(),
                ConfigLocation.systemProperties(),
                ConfigLocation.envFile(),
                ConfigLocation.workingDirectory(),
                ConfigLocation.jarDirectory(),
                ConfigLocation.classpath(),
                ConfigLocation.classpathLibraries()
        );
    }

    /**
     * Returns an order that reads only classpath library defaults and application classpath resources.
     *
     * @return classpath-only search order
     */
    public static SearchOrder classpathOnly() {
        return of(
                ConfigLocation.classpathLibraries(),
                ConfigLocation.classpath()
        );
    }

    /**
     * Returns a production-oriented external-first order.
     *
     * <p>This order favors externally supplied values under the default
     * {@link MergeStrategy#FILL_MISSING_ONLY}. It does not hardcode any /etc path.</p>
     *
     * @return production search order
     */
    public static SearchOrder production() {
        return externalFirst();
    }

    /**
     * Returns a test-oriented order that reads the working directory before classpath resources.
     *
     * @return test search order
     */
    public static SearchOrder test() {
        return of(
                ConfigLocation.workingDirectory(),
                ConfigLocation.classpath()
        );
    }

    /**
     * Returns an empty order.
     *
     * @return empty search order
     */
    public static SearchOrder none() {
        return new SearchOrder(List.of());
    }

    /**
     * Returns locations in read order.
     *
     * @return immutable location list
     */
    public List<ConfigLocation> locations() {
        return locations;
    }

    @Override
    public Iterator<ConfigLocation> iterator() {
        return locations.iterator();
    }

    @Override
    public String toString() {
        return locations.toString();
    }
}
