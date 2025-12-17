package io.github.eealba.example.webstore.infrastructure.config;

import io.github.eealba.payper.core.client.PayperAuthenticator;
import io.github.eealba.payper.core.client.PayperConfig;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import io.github.eealba.payper.payments.v2.api.PaymentsApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;
@Configuration
@Order(100)
public class PayperConfiguration {
    @Bean
    public PayperConfig payperConfig(PayperAuthenticator authenticator) {
        return PayperConfig.builder()
                           .authenticator(authenticator)
                           .connectTimeout(Duration.of(30, java.time.temporal.ChronoUnit.SECONDS))
                           .build();
    }
    @Bean
    public CheckoutOrdersApiClient checkoutOrdersApiClient(PayperConfig payperConfig) {
        return CheckoutOrdersApiClient.create(payperConfig);
    }

    @Bean
    public PaymentsApiClient paymentsApiClient(PayperConfig payperConfig) {
        return PaymentsApiClient.create(payperConfig);
    }

}
