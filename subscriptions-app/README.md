# Subscriptions with Payper — Java client for PayPal REST API

## Overview

This repository contains a concise, practical example that demonstrates how to create a product and a subscription plan using the Payper Java client library for PayPal's REST APIs. The example shows both synchronous and asynchronous implementations (`App.java` and `AppAsync.java`) so you can pick the integration style that best fits your application's concurrency model.

This document is written as a short technical article: it explains the example, highlights Payper's key benefits, and provides copy-paste steps to run the sample locally.

---

## Why Payper?

Payper is a focused Java client that simplifies integration with PayPal's REST endpoints. The examples in this project showcase common patterns and best practices you should adopt in production integrations.

Key benefits demonstrated by this example:

- OAuth 2.0 token management: automatic token acquisition, reuse, and refresh.
- Strongly-typed models: compile-time safety when building requests and reading responses.
- Fluent, discoverable API: builders and method chaining for readable code.
- Synchronous and asynchronous flavors: use the synchronous blocking API (`App.java`) or CompletableFuture-driven async API (`AppAsync.java`).
- Idempotency and tracing: sample usage of `paypal-request-id` to avoid duplicate operations.
- Clear error handling: responses expose success/error variants and HTTP status codes.
- Minimal boilerplate to create products and subscription billing plans.

---

## Prerequisites

- JDK 17 or later
- Maven
- A PayPal sandbox account (sandbox client ID and client secret)
- Network access to the PayPal sandbox endpoints

---

## Dependencies

Add the Payper modules required by the example to your project's `pom.xml`. This example uses the Subscriptions and Catalog Products modules. Replace `${payper.version}` with the latest Payper release (or the version you want to pin).

```xml
<dependencies>
  <dependency>
    <groupId>io.github.eealba.payper</groupId>
    <artifactId>payper-subscriptions-v1</artifactId>
    <version>${payper.version}</version>
  </dependency>
  <dependency>
    <groupId>io.github.eealba.payper</groupId>
    <artifactId>payper-catalog-products-v1</artifactId>
    <version>${payper.version}</version>
  </dependency>
</dependencies>
```

Note: the example project inside this repository already includes the minimal skeleton—use the provided code as a reference implementation.

---

## Credentials and Configuration

Create a properties file at `~/.payper/credentials.properties` with your sandbox credentials:

```properties
PAYPAL-CLIENT-ID=your-sandbox-client-id
PAYPAL-CLIENT-SECRET=your-sandbox-client-secret
```

The example code reads credentials from this file and configures a Payper authenticator for the PayPal sandbox. You can adapt the authentication setup to read from environment variables or a secrets manager as needed.

---

## What the example does

The sample demonstrates two flows:

1. Synchronous flow (`App.java`) — read credentials, create a product, then create a subscription plan associated with that product.
2. Asynchronous flow (`AppAsync.java`) — same operations but implemented using CompletableFuture to avoid blocking the calling thread.

Both flows build a product named "Video Streaming Service" and a simple "Basic Plan" that contains a trial cycle and a regular recurring billing cycle. The plan includes payment preferences (setup fee, auto-bill) and taxes.

---

## Key Implementation Highlights

- Credentials: `PayperAuthenticator.PayperAuthenticators.of(...)` is used to configure the sandbox endpoint and client credentials.
- Product creation: `CatalogProductsApiClient.create().products().create()` builds and sends a product request. The example uses `withPaypalRequestId(UUID.randomUUID().toString())` to set an idempotency/tracing header.
- Plan creation: `SubscriptionsApiClient.create().billingPlans().create()` builds a `PlanRequestPOST` containing billing cycles, pricing, payment preferences, and taxes.
- Response handling: examples show both `toResponse().toEntity()` and future-based `toFuture().thenApply(ResponseSpec.Response::toEntity)` flows.

---

## Example usage (Synchronous)

The synchronous example is implemented in `src/main/java/com/example/App.java`. High-level flow:

1. Load credentials from `~/.payper/credentials.properties`.
2. Set the default Payper authenticator.
3. Create a product via the Catalog Products API.
4. Create a billing plan tied to the product via the Subscriptions API.

Run the example locally:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.example.App"
```

Expected console output (example):

```text
Product created, id: PROD-7DW026709V5148032
Plan created, id: P-0460813545146854PM6FVEAY
```

---

## Example usage (Asynchronous)

The asynchronous example is implemented in `src/main/java/com/example/AppAsync.java`. It mirrors the same logic as `App.java` but returns CompletableFutures for the product and plan creation operations. Use this variant when you want non-blocking integration inside a reactive or high-throughput environment.

Run the async example:

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.example.AppAsync"
```

The example will print progress logs and final IDs when operations complete.

---

## Best practices and production notes

- Secrets management: avoid committing credentials. Use environment variables, a secure secrets manager, or a vault service in production.
- Retry and idempotency: use idempotency keys (paypal-request-id) for create operations to protect against duplicate requests.
- Error handling: inspect `ResponseSpec.Response` for error details and HTTP status codes to implement proper retries and fallback logic.
- Threading and scalability: use the async API for non-blocking server flows. The Payper client is designed to be thread-safe and to work in high-concurrency environments.
- Versioning: pin the Payper dependency to a specific release and test upgrades in a staging environment before rolling to production.

---

## Files of interest

- `src/main/java/com/example/App.java` — synchronous example (blocking)
- `src/main/java/com/example/AppAsync.java` — asynchronous example (CompletableFuture)
- `pom.xml` — minimal Maven project file (update with real Payper version as needed)

---

## Resources

- 💻 Example repository (this project): https://github.com/eealba/payper-examples/tree/main/subscriptions-app
- 📚 Payper library repository: https://github.com/eealba/payper
- 📝 Medium — Payper Part 1 (Getting started): https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-1-ca626d32875a
- 📝 Medium — Payper Part 2 (Orders / Examples): https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-2-orders-api-v2-1cae1dc8b7f0

---

## License

This example inherits the repository license. See the top-level `LICENSE` file for details.

