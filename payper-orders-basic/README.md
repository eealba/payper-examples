# Payper: Java Client for PayPal REST API - Part 2 (Orders API V2)

## Introduction

In this second article, we will delve into how to use Payper to interact with PayPal's REST API, focusing specifically on the **Orders API V2**. While PayPal offers a powerful API for managing orders, integrating it can be challenging. This article will guide you in leveraging Payper, a Java client that simplifies interaction with PayPal's Orders API in a clear and efficient way. 🚀

### What will you learn in this article?

- ✅ Create orders in PayPal
- ✅ Retrieve details of an existing order
- ✅ Confirm an order's payment method
- ✅ Capture the payment for an order

---

## Initial Setup

In the first article, we configured the credentials to communicate with PayPal using System Properties or Environment Variables. For this example, we will use a `credentials.properties` file created within a `.payper` folder in your home directory.

1. Create a file at the path `~/.payper/credentials.properties` with the following content, replacing the placeholders with your PayPal account details:

   ```properties
   PAYPAL-CLIENT-ID=<YOUR-PAYPAL-CLIENT-ID>
   PAYPAL-CLIENT-SECRET=<YOUR-PAYPAL-CLIENT-SECRET>
   ```

2. Once the file is created, you can create your client to read the credentials and point to the sandbox environment:

   ```java
   private void createClient() throws IOException {
       var credentialPath = Paths.get(System.getProperty("user.home"), ".payper", "credentials.properties");
       var authenticator = PayperAuthenticator.PayperAuthenticators.ofSandBox(credentialPath);
       var payperConfig = PayperConfig.builder().authenticator(authenticator).build();
       ordersApi = CheckoutOrdersApiClient.create(payperConfig).orders();
   }
   ```

---

## Orders V2 API

As indicated in PayPal's documentation, this API allows you to manage payment orders between two or more parties. Use the Orders API to create, update, retrieve, authorize, and capture orders.

Let's see how to perform the most common operations with PayPal's Orders API using Payper.

---

### 1. Create an Order 🛒

We will simplify the order creation process by specifying the minimum required fields. Here, we will create an order with a payment amount of $100.00.

```java
private String createOrder() {
    var orderRequest = OrderRequest.builder()
            .intent(CheckoutPaymentIntent.CAPTURE)
            .purchaseUnits(List.of(PurchaseUnitRequest.builder()
                    .amount(AmountWithBreakdown.builder()
                            .currencyCode(CurrencyCode.USD)
                            .value("100.00")
                            .build())
                    .build()))
            .build();

    var order = ordersApi.create().withBody(orderRequest).retrieve().toEntity();

    System.out.println("Order ID: " + order.id());
    return order.id();
}
```

---

### 2. Retrieve Order Details 🔍

After creating the order, you can retrieve its details using its ID.

```java
private void getOrderById(String orderId) {
    var order =  ordersApi.get().withId(orderId).retrieve().toEntity();
    System.out.println("Retrieved order with ID: " + order.id());
}
```

---

### 3. Confirm the Payment Method 💳

The next step is to confirm the payment source for the order. We will provide credit card details to confirm the payment.

```java
private void confirmPaymentSource(String orderId) {
    var body = ConfirmOrderRequest.builder()
            .paymentSource(PaymentSource.builder()
                    .card(CardRequest.builder()
                            .number("4111111111111111")
                            .expiry(new DateYearMonth("2035-01"))
                            .build())
                    .build())
            .build();
    var response = ordersApi.confirmPaymentSource().withId(orderId).withBody(body).retrieve().toResponse();
    if (response.isError()) {
        System.out.println("Error: " + response.toErrorEntity().message() + "status: " + response.statusCode());
        return;
    }
    System.out.println("Order confirmed");
}
```

---

### 4. Capture an Order 💰

Finally, we capture the payment associated with the order.

```java
private void captureOrder(String orderId) {
    var response = ordersApi.capture().withId(orderId).retrieve().toResponse();
    if (response.isError()) {
        System.out.println("Error: " + response.toErrorEntity().message() + "status: " + response.statusCode());
        return;
    }
    System.out.println("Capture ID: " + response.toEntity().id());
}
```

---

## Conclusion

With this example, we have explored how to interact with PayPal's Orders API using **Payper**. This workflow includes creating, retrieving, confirming, and capturing orders, making it easier to integrate PayPal into your Java applications.

Ready to simplify your PayPal integrations? 💡 Try Payper today!

---

## Resources

- 📝 **Original Article**: [Payper: Java Client for PayPal REST API - Part 2 (Orders API V2)](https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-2-orders-api-v2-e7e9ef3f4d00)
- 💻 **Complete Code**: [View on GitHub](https://github.com/eealba/payper-examples/tree/main/payper-orders-basic)
- 📚 **Payper Library**: [GitHub Repository](https://github.com/eealba/payper)
- 📖 **Previous Article**: [Part 1 - Getting Started](https://medium.com/@eealba/payper-java-client-for-paypal-rest-api-part-1-ca626d32875a?source=friends_link&sk=7027166d2f1ce506f47659aabd1eb0dd)
