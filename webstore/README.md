# Webstore Project

This repository contains a small demo web store application — a proof-of-concept (PoC) that showcases a catalog, checkout flow and a backoffice demo for order management. The project demonstrates integrating PayPal payments using the [Payper Java SDK](https://github.com/eealba/payper) (payper-orders-v2 and payper-payments-v2).

> Author: Edgar Enrique Alba Barrile
> 
> Public project — no commercial support is provided.

## Disclaimer
This project is an example PoC using the Payper Java SDK and PayPal APIs. It is provided "as is" for demonstration purposes only. No warranties or guarantees are offered. Use at your own risk.

## Project overview
A demo web store for online product sales, featuring a simple product catalog, a checkout process with delivery address and payment method selection, and a backoffice that lists orders and allows capture of authorized payments.

## Key Features
- Product catalog with dynamic product cards
- Checkout process with delivery address and payment method selection (PayPal or credit card)
- Payment integration through PayPal using the Payper Java SDK
- Backoffice demo to view orders and capture authorized payments
- REST API endpoints documented with OpenAPI (see `src/main/resources/openapi/openapi.yml`)

## Technology stack
- Java 21
- Spring Boot (backend web application and REST APIs)
- [Payper Java SDK](https://github.com/eealba/payper) (payper-orders-v2 and payper-payments-v2) — used for payment/order integration
- MapStruct (DTO mapping)
- Lombok
- Springdoc OpenAPI / Swagger for API docs
- Jackson for JSON serialization
- Bootstrap 5 for frontend styling

## Requirements
- Java (as used in the project — see `pom.xml` for the exact version used)
- A PayPal API credential (Client ID and Client Secret)

How to obtain PayPal credentials (from PayPal docs):
1. Go to the PayPal Developer Dashboard: https://developer.paypal.com/dashboard/ and log in or sign up.
2. Select **Apps & Credentials**.
3. New accounts come with a Default Application in the REST API apps section. To create a new project, select **Create App**.
4. Copy the **Client ID** and **Client Secret** for your app.
5. Create the file `credentials.properties` in `~/.payper/` (this is how `LocalSystemPropertyConfig` expects credentials) with the following content:

```
PAYPAL-CLIENT-ID=<clientID>
PAYPAL-CLIENT-SECRET=<secret>
```

Replace `<clientID>` and `<secret>` with the credentials from the PayPal dashboard.

## How to use (quick)
1. Start the application from the project root:

```bash
./mvnw spring-boot:run
```

2. Open the UI in your browser:
- Webstore (customer-facing): http://localhost:8080/webstore.html (or `webstore.html` from the root)
- Backoffice demo: http://localhost:8080/backoffice.html
- API docs (Swagger UI / OpenAPI): http://localhost:8080/swagger-ui/index.html

3. Use the Backoffice demo to fetch orders and order history, and to capture authorized payments.

## Important API endpoints (examples)
- GET /api/catalog — retrieve the product catalog (used by the storefront)
- POST /api/checkout/orders — submit a checkout order (body follows `OrderRequestDTO` schema)
- POST /api/checkout/orders/{referenceId}/capture — capture an order payment
- POST /api/checkout/orders/{referenceId}/authorize — authorize an order payment
- GET /api/backoffice/orders — list current orders
- GET /api/backoffice/orders/history — list orders history (most recent first)
- POST /api/payments/authorizations/{referenceId}/capture — capture an authorized payment (used by backoffice capture flow)

See `src/main/resources/openapi/openapi.yml` for full DTO schemas and API documentation.

## Execution Flow

For a detailed, step-by-step execution flow (including PayPal-account redirect vs credit-card direct capture, frontend JS interactions and backend call graph with a Mermaid sequence diagram), see: [Execution Flow — WEBSTORE_FLOW.md](WEBSTORE_FLOW.md)

This document explains how `app.js` and `backapp.js` interact with the backend controllers, the core use-cases, and the `PayPalPaymentGateway` that uses the Payper SDK.

## Development notes
- The product data used by the frontend is stored in `src/main/resources/data/products.json`.
- Frontend JavaScript lives in `src/main/resources/static/app.js` and `src/main/resources/static/backapp.js`.
- The in-memory order repository stores order history (most recent first) and is implemented in `MemoryOrderRepositoryImpl`.

## Author
Edgar Enrique Alba Barrile

## License
This repository is public and intended for demonstration purposes only. No warranty is provided.
