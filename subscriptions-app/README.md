# Using Payper Java Client Library to Consume PayPal REST API

This tutorial will guide you through the process of using the Payper Java client library to create a product and a subscription plan using Java. We will use the `App.java` class as an example.



### Step 1: Set Up Your Project

1. Create a new Maven project using the following command:

    ```sh
    mvn archetype:generate -DgroupId=com.example -DartifactId=subscriptions-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
    ```

2. Add the following dependencies to your `pom.xml` file:

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

### Step 2: Set Up Your Credentials

1. Create a file named `credentials.properties` in your home directory under `.payper` folder with the following content:

    ```properties
    PAYPAL-CLIENT-ID=your-client-id
    PAYPAL-CLIENT-SECRET=your-client-secret
    ```

### Step 3: Implement the `App.java` Class

1. Create a new Java class named `App.java` in the `src/main/java/com/example` directory with the following content:

    ```java
    package com.example;

    import io.github.eealba.payper.catalog.products.v1.api.CatalogProductsApiClient;
    import io.github.eealba.payper.catalog.products.v1.model.Product;
    import io.github.eealba.payper.catalog.products.v1.model.ProductCategory;
    import io.github.eealba.payper.catalog.products.v1.model.ProductRequestPOST;
    import io.github.eealba.payper.core.client.PayperAuthenticator;
    import io.github.eealba.payper.subscriptions.v1.api.SubscriptionsApiClient;
    import io.github.eealba.payper.subscriptions.v1.model.BillingCycle;
    import io.github.eealba.payper.subscriptions.v1.model.CurrencyCode;
    import io.github.eealba.payper.subscriptions.v1.model.Frequency;
    import io.github.eealba.payper.subscriptions.v1.model.Money;
    import io.github.eealba.payper.subscriptions.v1.model.PaymentPreferences;
    import io.github.eealba.payper.subscriptions.v1.model.Percentage;
    import io.github.eealba.payper.subscriptions.v1.model.Plan;
    import io.github.eealba.payper.subscriptions.v1.model.PlanRequestPOST;
    import io.github.eealba.payper.subscriptions.v1.model.PricingScheme;
    import io.github.eealba.payper.subscriptions.v1.model.Taxes;

    import java.io.FileInputStream;
    import java.io.IOException;
    import java.util.List;
    import java.util.Properties;
    import java.util.UUID;

    import static io.github.eealba.payper.core.client.PayperAuthenticator.PayperAuthenticators.API_SANDBOX_PAYPAL_COM;

    public class App {
        public static void main(String[] args) throws IOException {
            setCredentials();
            var product = createProduct();
            var plan = createPlan(product);
        }

        private static void setCredentials() throws IOException {
            Properties props = new Properties();
            props.load(new FileInputStream(System.getProperty("user.home") + "/.payper/credentials.properties"));
            var payperAuthenticator = PayperAuthenticator.PayperAuthenticators.of(
                    () -> API_SANDBOX_PAYPAL_COM,
                    props.getProperty("PAYPAL-CLIENT-ID")::toCharArray,
                    props.getProperty("PAYPAL-CLIENT-SECRET")::toCharArray
            );
            PayperAuthenticator.PayperAuthenticators.setDefault(payperAuthenticator);
        }

        private static Product createProduct() {
            var product = ProductRequestPOST.builder()
                    .name("Video Streaming Service")
                    .description("A video streaming service")
                    .type(ProductRequestPOST.Type.SERVICE)
                    .category(ProductCategory.SOFTWARE)
                    .imageUrl("https://example.com/streaming.jpg")
                    .homeUrl("https://example.com/home")
                    .build();

            var response = CatalogProductsApiClient.create().products().create()
                    .withPaypalRequestId(UUID.randomUUID().toString())
                    .withBody(product)
                    .retrieve()
                    .toResponse();

            var productResponse = response.toEntity();
            System.out.println("Product created, id: " + productResponse.id());
            return productResponse;
        }

        private static Plan createPlan(Product product) {
            var plan = PlanRequestPOST.builder()
                    .productId(product.id())
                    .name("Basic Plan")
                    .description("A basic plan")
                    .billingCycles(List.of(
                            BillingCycle.builder()
                                    .frequency(Frequency.builder().intervalUnit(Frequency.IntervalUnit.MONTH).intervalCount(1).build())
                                    .tenureType(BillingCycle.TenureType.TRIAL)
                                    .sequence(1)
                                    .totalCycles(1)
                                    .build(),
                            BillingCycle.builder()
                                    .frequency(Frequency.builder().intervalUnit(Frequency.IntervalUnit.MONTH).intervalCount(1).build())
                                    .tenureType(BillingCycle.TenureType.REGULAR)
                                    .sequence(2)
                                    .totalCycles(12)
                                    .pricingScheme(PricingScheme.builder()
                                            .fixedPrice(new Money(CurrencyCode.USD, "10"))
                                            .build())
                                    .build()
                            ))
                    .paymentPreferences(PaymentPreferences.builder()
                            .autoBillOutstanding(true)
                            .setupFee(new Money(CurrencyCode.USD, "10"))
                            .setupFeeFailureAction(PaymentPreferences.SetupFeeFailureAction.CONTINUE)
                            .paymentFailureThreshold(3)
                            .build())
                    .taxes(Taxes.builder()
                            .percentage(new Percentage("10"))
                            .inclusive(false)
                            .build())
                    .build();

            var response = SubscriptionsApiClient.create().billingPlans().create()
                    .withPaypalRequestId(UUID.randomUUID().toString())
                    .withBody(plan)
                    .retrieve()
                    .toResponse();

            var planResponse = response.toEntity();
            System.out.println("Plan created, id: " + planResponse.id());
            return planResponse;
        }
    }
    ```

### Step 4: Run the Application

1. Compile and run the application using the following Maven command:

    ```sh
    mvn clean install
    mvn exec:java -Dexec.mainClass="com.example.App"
    ```

### Result

After running the application, you should see the following output in the console:

```text
Product created, id: PROD-7DW026709V5148032
Plan created, id: P-0460813545146854PM6FVEAY