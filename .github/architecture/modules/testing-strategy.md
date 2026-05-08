# Testing Strategy Module

Tests should cover public behavior without depending on the real machine environment. Environment variables and system properties are abstracted so tests can pass deterministic maps.

Do not weaken tests to make a build pass.

Release-sensitive tests should cover search order presets, env opt-in behavior, profile filename expansion, merge strategies, duplicate key behavior, interpolation errors, record mapping, annotations, type conversion, secret masking, report generation, and zero production dependencies.
