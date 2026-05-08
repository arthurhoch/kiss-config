package io.github.arthurhoch.kissconfig.internal.source;

import io.github.arthurhoch.kissconfig.ConfigLocation;
import io.github.arthurhoch.kissconfig.ConfigLocationType;
import io.github.arthurhoch.kissconfig.KissConfig;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * Resolves configured locations into concrete source candidates.
 */
public final class ConfigSourceResolver {
    private static final String LIBRARY_DEFAULTS = "META-INF/kiss-config/defaults.properties";

    private ConfigSourceResolver() {
    }

    public static List<SourceCandidate> resolve(LoadOptions options) {
        List<SourceCandidate> candidates = new ArrayList<>();
        for (ConfigLocation location : options.searchOrder()) {
            switch (location.type()) {
                case CLASSPATH -> addClasspath(candidates, options);
                case CLASSPATH_LIBRARIES -> addClasspathLibraries(candidates, options);
                case JAR_DIRECTORY -> addJarDirectory(candidates, options);
                case WORKING_DIRECTORY -> addDirectory(candidates, ConfigLocationType.WORKING_DIRECTORY, workingDirectory(), options);
                case EXPLICIT_PATH -> addDirectory(candidates, ConfigLocationType.EXPLICIT_PATH, location.path().orElseThrow(), options);
                case EXPLICIT_FILE -> addExplicitFile(candidates, location.path().orElseThrow());
                case ENV_FILE -> addEnvFileLocation(candidates, location, options);
                case SYSTEM_PROPERTIES -> candidates.add(SourceCandidate.map(
                        "Java system properties",
                        ConfigLocationType.SYSTEM_PROPERTIES,
                        options.systemProperties()
                ));
                case ENVIRONMENT_VARIABLES -> candidates.add(SourceCandidate.map(
                        "Environment variables",
                        ConfigLocationType.ENVIRONMENT_VARIABLES,
                        options.environmentVariables()
                ));
            }
        }
        return candidates;
    }

    private static void addClasspath(List<SourceCandidate> candidates, LoadOptions options) {
        for (Path path : profileCandidates(options.propertyFile(), options.profile(), false)) {
            String resource = toResourceName(path);
            URL url = options.classLoader().getResource(resource);
            if (url == null) {
                candidates.add(SourceCandidate.missingClasspath("classpath:" + resource, ConfigLocationType.CLASSPATH));
            } else {
                candidates.add(SourceCandidate.url("classpath:" + resource, ConfigLocationType.CLASSPATH, url, SourceFormat.PROPERTIES));
            }
        }
    }

    private static void addClasspathLibraries(List<SourceCandidate> candidates, LoadOptions options) {
        try {
            Enumeration<URL> urls = options.classLoader().getResources(LIBRARY_DEFAULTS);
            boolean found = false;
            while (urls.hasMoreElements()) {
                found = true;
                URL url = urls.nextElement();
                candidates.add(SourceCandidate.url(url.toExternalForm(), ConfigLocationType.CLASSPATH_LIBRARIES, url, SourceFormat.PROPERTIES));
            }
            if (!found) {
                candidates.add(SourceCandidate.missingClasspath("classpath*:" + LIBRARY_DEFAULTS, ConfigLocationType.CLASSPATH_LIBRARIES));
            }
        } catch (Exception e) {
            candidates.add(SourceCandidate.skipped(
                    "classpath*:" + LIBRARY_DEFAULTS,
                    ConfigLocationType.CLASSPATH_LIBRARIES,
                    "Skipped classpath library defaults: " + e.getMessage()
            ));
        }
    }

    private static void addJarDirectory(List<SourceCandidate> candidates, LoadOptions options) {
        Path jarDir = jarDirectory();
        if (jarDir == null) {
            candidates.add(SourceCandidate.skipped(
                    "JAR directory",
                    ConfigLocationType.JAR_DIRECTORY,
                    "Skipped because the application is not running from a JAR"
            ));
            return;
        }
        addDirectory(candidates, ConfigLocationType.JAR_DIRECTORY, jarDir, options);
    }

    private static void addDirectory(
            List<SourceCandidate> candidates,
            ConfigLocationType type,
            Path directory,
            LoadOptions options
    ) {
        for (Path property : profileCandidates(options.propertyFile(), options.profile(), false)) {
            Path resolved = directory.resolve(property).normalize();
            candidates.add(SourceCandidate.file(resolved.toString(), type, resolved, SourceFormat.PROPERTIES, false, true));
        }
        if (options.envFile() != null) {
            for (Path env : profileCandidates(options.envFile(), options.profile(), true)) {
                Path resolved = directory.resolve(env).normalize();
                candidates.add(SourceCandidate.file(resolved.toString(), type, resolved, SourceFormat.ENV, false, true));
            }
        }
    }

    private static void addExplicitFile(List<SourceCandidate> candidates, Path file) {
        SourceFormat format = looksLikeEnvFile(file) ? SourceFormat.ENV : SourceFormat.PROPERTIES;
        candidates.add(SourceCandidate.file(file.toString(), ConfigLocationType.EXPLICIT_FILE, file, format, true, false));
    }

    private static void addEnvFileLocation(List<SourceCandidate> candidates, ConfigLocation location, LoadOptions options) {
        if (location.path().isPresent()) {
            Path explicit = location.path().orElseThrow();
            candidates.add(SourceCandidate.file(explicit.toString(), ConfigLocationType.ENV_FILE, explicit, SourceFormat.ENV, true, false));
            return;
        }
        Path envFile = options.envFile();
        if (envFile == null) {
            candidates.add(SourceCandidate.skipped(
                    "Configured env file",
                    ConfigLocationType.ENV_FILE,
                    "Skipped because no env file was configured"
            ));
            return;
        }
        candidates.add(SourceCandidate.file(envFile.toString(), ConfigLocationType.ENV_FILE, envFile, SourceFormat.ENV, true, false));
        if (options.profile() != null) {
            Path profileVariant = profileVariant(envFile, options.profile(), true);
            candidates.add(SourceCandidate.file(profileVariant.toString(), ConfigLocationType.ENV_FILE, profileVariant, SourceFormat.ENV, false, true));
        }
    }

    private static List<Path> profileCandidates(Path base, String profile, boolean envStyle) {
        if (profile == null) {
            return List.of(base);
        }
        return List.of(base, profileVariant(base, profile, envStyle));
    }

    public static Path profileVariant(Path base, String profile, boolean envStyle) {
        Path fileName = base.getFileName();
        String name = fileName == null ? base.toString() : fileName.toString();
        String profiled;
        if (envStyle && ".env".equals(name)) {
            profiled = ".env." + profile;
        } else {
            int dot = name.lastIndexOf('.');
            if (dot > 0) {
                profiled = name.substring(0, dot) + "-" + profile + name.substring(dot);
            } else {
                profiled = name + "-" + profile;
            }
        }
        Path parent = base.getParent();
        return parent == null ? Path.of(profiled) : parent.resolve(profiled);
    }

    private static boolean looksLikeEnvFile(Path file) {
        String name = file.getFileName() == null ? file.toString() : file.getFileName().toString();
        String lower = name.toLowerCase(Locale.ROOT);
        return lower.equals(".env") || lower.startsWith(".env.") || lower.endsWith(".env");
    }

    private static String toResourceName(Path path) {
        return path.toString().replace(File.separatorChar, '/').replaceFirst("^/+", "");
    }

    private static Path workingDirectory() {
        return Path.of("").toAbsolutePath().normalize();
    }

    private static Path jarDirectory() {
        try {
            CodeSource codeSource = KissConfig.class.getProtectionDomain().getCodeSource();
            if (codeSource == null || codeSource.getLocation() == null) {
                return null;
            }
            Path location = Path.of(codeSource.getLocation().toURI()).toAbsolutePath().normalize();
            if (location.toString().endsWith(".jar")) {
                return location.getParent();
            }
            return null;
        } catch (URISyntaxException | IllegalArgumentException e) {
            return null;
        }
    }
}
