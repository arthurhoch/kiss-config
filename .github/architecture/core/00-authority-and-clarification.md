# Authority And Clarification

The issue, prompt, or maintainer request that defines a change is authoritative. If a detail is missing, choose the simplest production-safe behavior, document it, and test it.

Architecture decisions in this repository are binding until changed by maintainers. Public docs, `docs/AI_PROJECT_MANUAL.md`, `PROJECT_CONTEXT.md`, tests, and Javadocs must match implementation behavior.

If documentation and code disagree, do not guess. Inspect the implementation and tests, then either fix the docs or fix the code depending on the authoritative product decision.
