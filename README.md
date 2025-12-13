# Payper Examples — Reference and Quick Guide

[![License](https://img.shields.io/github/license/eealba/payper-examples)](LICENSE) ![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)

This repository contains small, focused example projects that demonstrate how to integrate Java applications with PayPal using the Payper SDK (Payper Java client). Each subfolder is a standalone example with a dedicated `README.md` that explains the implementation and how to run it.

Purpose: provide practical, copy-paste-ready code that illustrates common PayPal integration scenarios using the Payper SDK — from a 5-minute quickstart to Orders and Subscription flows.

---

## Quick index

| Example | Summary | Link |
|---|---|---|
| payper-5-minutes | 5-minute quickstart to validate sandbox credentials and perform a simple Catalog Products API call. | [Open](payper-5-minutes/) |
| payper-orders-basic | Demonstrates Orders API v2: create, retrieve, confirm payment source, capture. | [Open](payper-orders-basic/) |
| subscriptions-app | Demonstrates product and subscription plan creation (sync `App.java` and async `AppAsync.java`). | [Open](subscriptions-app/) |
| webstore | Spring Boot demo webstore showcasing catalog, checkout and backoffice integrations (OpenAPI included). | [Open](webstore/) |

---

## Examples (at a glance)

- payper-5-minutes/
  - A compact, 5-minute quickstart demonstrating how to configure Payper and make a simple API call (Catalog Products API). Ideal to get started and validate your sandbox credentials quickly.
  - See: [payper-5-minutes](payper-5-minutes/)

- payper-orders-basic/
  - A hands-on example focused on the Orders API v2. Covers creating orders, retrieving order details, confirming payment sources, and capturing payments. Includes synchronous client usage and error handling patterns.
  - See: [payper-orders-basic](payper-orders-basic/)

- subscriptions-app/
  - Subscription and billing plans example. Demonstrates product creation and subscription plan creation with both synchronous (`App.java`) and asynchronous (`AppAsync.java`) implementations.
  - See: [subscriptions-app](subscriptions-app/)

- webstore/
  - A small demonstration web store (Spring Boot) that integrates product catalog and checkout flows using Payper (orders and payments). Includes a backoffice demo and OpenAPI documentation.
  - See: [webstore](webstore/)

---

## How to use this repository

1. Pick the example you want to explore and open its folder.
2. Read the example `README.md` for prerequisites and detailed instructions.
3. Create a credentials file at `~/.payper/credentials.properties` with your sandbox client id and secret, or follow the example-specific instructions for credentials and configuration.
4. Build and run the project following the instructions inside the chosen example directory.

---

## Resources

- Payper SDK (repository): https://github.com/eealba/payper
- Payper SDK Wiki: https://github.com/eealba/payper/wiki
- Example repository (this project): https://github.com/eealba/payper-examples
- Medium — Payper Part 1 (Getting started / 5-minute guide): https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-1-ca626d32875a
- Medium — Payper Part 2 (Orders API examples): https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-2-orders-api-v2-1cae1dc8b7f0

---

## License

This repository follows the license declared in the top-level `LICENSE` file. The examples are provided for learning and demonstration purposes.
