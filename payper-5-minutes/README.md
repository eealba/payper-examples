# Payper: Java Client for PayPal REST API - Part 1

## Introduction

This article introduces **Payper**, an open-source Java library designed to greatly simplify the complexity of integrating with PayPal's extensive suite of more than 15 REST APIs.

If you're beginning the challenging journey of integrating your Java back-end solution with PayPal, consider using this library. It significantly reduces development time, enabling you to focus on your core business. Payper is also useful if you already have an existing integration but need to add new APIs or replace outdated ones with newer versions.

### Key Features of Payper

- **OAuth 2.0 Token Manager:** All calls to PayPal's REST API require an access token, as they use OAuth 2.0. Payper automatically obtains the necessary token, reuses it, and manages its renewal when it expires.

- **Optimized for High Concurrency:** Designed for multithreading environments, Payper efficiently handles high-concurrency applications.

- **Support for Synchronous and Asynchronous Calls:** You can perform synchronous calls or take advantage of asynchronous support using `CompletableFuture`, depending on your application's needs.

With these features, Payper becomes a powerful and flexible tool for Java developers working with PayPal's REST API.

---

## Payper in 5 Minutes: Step-by-Step Guide

### Prerequisites

Before you begin, ensure you have the following:

- **Java Development Kit (JDK) 17 or higher**
- **Maven**
- A PayPal sandbox account
- PayPal Sandbox credentials (client ID and client secret)

### Step 1: Create a New Project

Run the following command to generate a new Maven project:

```sh
mvn archetype:generate -DgroupId=com.example -DartifactId=payper-5-minutes -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### Step 2: Add the Payper Dependency

For each PayPal API, Payper provides a specific module. In this example, we will use the **Catalog Products API** module. Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.eealba.payper</groupId>
    <artifactId>payper-catalog-products-v1</artifactId>
    <version>0.4.0</version>
</dependency>
```

### Step 3: Set Up PayPal Sandbox Credentials

Payper offers several ways to specify PayPal credentials and determine whether to connect to the Sandbox or Live (Production) environment. The simplest approach is to use System Properties or Environment Variables. In this example, we'll set the System Properties programmatically:

```java
// Replace the placeholders with your own values
System.setProperty("PAYPAL-CLIENT-ID", "<PAYPAL-CLIENT-ID>");
System.setProperty("PAYPAL-CLIENT-SECRET", "<PAYPAL-CLIENT-SECRET>");
System.setProperty("PAYPAL-BASE-URL", "https://api.sandbox.paypal.com");
```

### Step 4: Make a Request

The following example demonstrates how to create a product using the Catalog Products API:

```java
var catalogProductsApiClient = CatalogProductsApiClient.create();

var productRequest = ProductRequestPOST.builder()
       .name("Product Name")
       .description("Product Description")
       .type(ProductRequestPOST.Type.PHYSICAL)
       .category(ProductCategory.ACCESSORIES)
       .imageUrl("https://example.com/image.jpg")
       .build();

var product = catalogProductsApiClient.products()
       .create()
       .withBody(productRequest)
       .retrieve()
       .toEntity();

```

### Step 5: Run the Code

Run your program, and the output should resemble the following:

```text
Created product ID: PROD-9A103744078216213
```

Congratulations! If you've followed the steps correctly, you've successfully made your first call to PayPal's REST API in just 5 minutes.

---

## Conclusion

This introductory guide highlights the versatility of Payper, enabling you to minimize integration time and focus on what matters most to your business. For the complete code used in this guide, visit the following links:

- [Example Code Repository](https://github.com/eealba/payper-examples/tree/main/payper-5-minutes)
- [Payper Library Repository](https://github.com/eealba/payper)

---

Original article: [Payper: Java Client for PayPal REST API - Part 1](https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-1-ca626d32875a?source=friends_link&sk=7027166d2f1ce506f47659aabd1eb0dd)
