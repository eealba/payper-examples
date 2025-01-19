package com.example;

import io.github.eealba.payper.catalog.products.v1.api.CatalogProductsApiClient;
import io.github.eealba.payper.catalog.products.v1.model.Product;
import io.github.eealba.payper.catalog.products.v1.model.ProductCategory;
import io.github.eealba.payper.catalog.products.v1.model.ProductRequestPOST;
import io.github.eealba.payper.core.client.PayperAuthenticator;
import io.github.eealba.payper.core.client.ResponseSpec;
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
import java.util.concurrent.CompletableFuture;

import static io.github.eealba.payper.core.client.PayperAuthenticator.PayperAuthenticators.API_SANDBOX_PAYPAL_COM;

public class AppAsync {
    public static void main(String[] args) throws IOException {
        setCredentials();
        createProduct()
                .thenApply(product -> {
                    System.out.println("Product created, id: " + product.id());
                    return product;
                })
                .thenCompose(AppAsync::createPlan)
                .thenAccept(plan -> {
                    System.out.println("Plan created, id: " + plan.id());
                })
                .exceptionally(ex -> {
                    System.err.println("An error occurred: " + ex.getMessage());
                    return null;
                })
                .join();
    }


    private static void setCredentials() throws IOException {
        // Read the credentials from a file
        Properties props = new Properties();
        props.load(new FileInputStream(System.getProperty("user.home") + "/.payper/credentials.properties"));
        var payperAuthenticator = PayperAuthenticator.PayperAuthenticators.of(
                () -> API_SANDBOX_PAYPAL_COM,
                props.getProperty("PAYPAL-CLIENT-ID")::toCharArray,
                props.getProperty("PAYPAL-CLIENT-SECRET")::toCharArray
        );
        PayperAuthenticator.PayperAuthenticators.setDefault(payperAuthenticator);

    }
    private static CompletableFuture<Product> createProduct() {
        var product = ProductRequestPOST.builder()
                .name("Video Streaming Service")
                .description("A video streaming service")
                .type(ProductRequestPOST.Type.SERVICE)
                .category(ProductCategory.SOFTWARE)
                .imageUrl("https://example.com/streaming.jpg")
                .homeUrl("https://example.com/home")
                .build();

        return CatalogProductsApiClient.create().products().create()
                .withPaypalRequestId(UUID.randomUUID().toString())
                .withBody(product)
                .retrieve()
                .toFuture()
                .thenApply(ResponseSpec.Response::toEntity);
    }

    private static CompletableFuture<Plan> createPlan(Product product) {
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

        return SubscriptionsApiClient.create().billingPlans().create()
                .withPaypalRequestId(UUID.randomUUID().toString())
                .withBody(plan)
                .retrieve()
                .toFuture()
                .thenApply(ResponseSpec.Response::toEntity);
    }
}
