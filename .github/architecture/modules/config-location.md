# Config Location Module

Supported location types are classpath, classpath library defaults, jar directory, working directory, explicit directory, explicit file, explicit env file, system properties, and environment variables.

Directory locations discover property files and profile property files. They discover env files only when env loading is explicitly configured.

Explicit missing files fail by default. Auto-discovered missing files are skipped.
