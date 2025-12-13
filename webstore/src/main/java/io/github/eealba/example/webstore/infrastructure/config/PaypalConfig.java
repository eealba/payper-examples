package io.github.eealba.example.webstore.infrastructure.config;

import io.github.eealba.payper.core.client.PayperAuthenticator;
import io.github.eealba.payper.core.client.PayperConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.time.Duration;
@Configuration
@Order(100)
public class PaypalConfig {
    @Bean
    public PayperConfig payperConfig(PayperAuthenticator authenticator) {
        return PayperConfig.builder()
                           .authenticator(authenticator)
                           .connectTimeout(Duration.of(30, java.time.temporal.ChronoUnit.SECONDS))
                           .build();
    }

}
