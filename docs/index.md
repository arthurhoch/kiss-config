---
layout: default
title: KissConfig
---

<section class="hero">
  <div>
    <p class="eyebrow">KISS Java Libraries</p>
    <h1>KissConfig</h1>
    <p class="lead">Tiny zero-dependency Java 17+ configuration loading from properties, explicit .env files, system properties, and environment variables into immutable Java records.</p>
    <div class="meta-row">
      <span class="tag">Latest documented release: 0.1.0</span>
      <span class="tag">Java 17+</span>
      <span class="tag">Apache-2.0</span>
    </div>
    <div class="actions">
      <a class="button" href="getting-started.html">Getting Started</a>
      <a class="button secondary" href="api.html">API Reference</a>
      <a class="button secondary" href="https://github.com/arthurhoch/kiss-config">GitHub</a>
    </div>
  </div>
  <div class="panel">
    <p class="panel-title">Maven</p>
<pre><code>&lt;dependency&gt;
  &lt;groupId&gt;io.github.arthurhoch&lt;/groupId&gt;
  &lt;artifactId&gt;kiss-config&lt;/artifactId&gt;
  &lt;version&gt;0.1.0&lt;/version&gt;
&lt;/dependency&gt;</code></pre>
  </div>
</section>

<section class="section two-column">
  <div>
    <h2>Small Surface</h2>
    <p>KissConfig makes source order, merge behavior, interpolation, mapping, and secret masking explicit. Records are the primary mapping target for the documented 0.1.0 API.</p>
  </div>
  <div class="panel">
    <p class="panel-title">Quick Example</p>
<pre><code>public record AppConfig(ServerConfig server) {}
public record ServerConfig(String host, int port) {}

AppConfig config = KissConfig.load(AppConfig.class);</code></pre>
  </div>
</section>

<section class="section">
  <h2>KISS Principles</h2>
  <div class="feature-grid">
    <article class="feature">
      <h3>Predictable Order</h3>
      <p>Search order and merge strategy are explicit instead of hidden behind framework defaults.</p>
    </article>
    <article class="feature">
      <h3>Records First</h3>
      <p>Immutable Java records keep configuration shape compact and easy to audit.</p>
    </article>
    <article class="feature">
      <h3>Secret Aware</h3>
      <p>Reports and exceptions mask values marked or detected as secret.</p>
    </article>
  </div>
</section>

<section class="section">
  <h2>Documentation</h2>
  <div class="doc-grid">
    <a href="getting-started.html">Getting Started<span>Install and load the first config.</span></a>
    <a href="api.html">API Reference<span>Public API and result types.</span></a>
    <a href="search-order.html">Search Order<span>Source ordering and presets.</span></a>
    <a href="config-locations.html">Config Locations<span>File, classpath, and explicit locations.</span></a>
    <a href="merge-strategy.html">Merge Strategy<span>Conflict behavior and source metadata.</span></a>
    <a href="profiles.html">Profiles<span>Profile validation and filename expansion.</span></a>
    <a href="env-files.html">Env Files<span>Explicit .env loading behavior.</span></a>
    <a href="properties-files.html">Properties Files<span>Supported Java properties syntax.</span></a>
    <a href="interpolation.html">Interpolation<span>Variables, defaults, and cycles.</span></a>
    <a href="mapping.html">Mapping<span>Record mapping and conversion behavior.</span></a>
    <a href="secrets.html">Secrets<span>Masking rules and annotations.</span></a>
    <a href="security-hardening.html">Security Hardening<span>Repository hardening and local quality commands.</span></a>
    <a href="code-cleanup.html">Safe Code Cleanup<span>Deletion policy and quality gates.</span></a>
    <a href="release.html">Release<span>Release process and Maven Central flow.</span></a>
    <a href="testing-report.html">Testing Report<span>Current verification state.</span></a>
    <a href="javadocs/">Javadocs<span>Generated API documentation.</span></a>
  </div>
</section>

<section class="section">
  <h2>Related Projects</h2>
  <div class="related-grid">
    <a href="https://github.com/arthurhoch/kiss-json">kiss-json<span>Field-based JSON serialization and deserialization.</span></a>
    <a href="https://github.com/arthurhoch/kiss-requests">kiss-requests<span>Simple HTTP client built on Java HttpClient.</span></a>
    <a href="https://github.com/arthurhoch/kiss-server">kiss-server<span>Small HTTP/1.1 server for simple REST-style applications.</span></a>
    <a href="https://github.com/arthurhoch/kiss-config">kiss-config<span>Configuration from properties, .env, system properties, and environment variables.</span></a>
    <a href="https://github.com/arthurhoch/kiss-binary">kiss-binary<span>Explicit binary IO for primitive binary formats.</span></a>
  </div>
</section>
