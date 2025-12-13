package io.github.eealba.example.webstore.infrastructure.config;

import io.github.eealba.payper.core.client.PayperAuthenticator;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Profile("!local")
@Order(5)
public class SystemPropertyConfig {
    private PayperAuthenticator authenticator;

    @PostConstruct
    public void setSystemProperties() throws IOException {
        String clientId = System.getenv("PAYPAL_CLIENT_ID");
        String secret = System.getenv("PAYPAL_CLIENT_SECRET");
        if (clientId != null && secret != null) {
            authenticator = PayperAuthenticator.PayperAuthenticators
                    .ofSandBox(clientId::toCharArray,secret::toCharArray);
        }else{
            Path path = Paths.get(System.getProperty("user.home"), ".payper", "credentials.properties");
            if (Files.exists(path)) {
                authenticator = PayperAuthenticator.PayperAuthenticators.ofSandBox(path);
            }else {
                throw new IllegalStateException("PAYPAL_CLIENT_ID and PAYPAL_CLIENT_SECRET environment " +
                                                        "variables must be set or credentials file must exist");
            }
        }

    }
    @Bean
    public PayperAuthenticator getPayperAuthenticator() {
        return authenticator;
    }

}