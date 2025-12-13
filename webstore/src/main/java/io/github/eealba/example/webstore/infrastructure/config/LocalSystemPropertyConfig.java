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
@Profile("local")
@Order(5)
public class LocalSystemPropertyConfig {

    private PayperAuthenticator authenticator;

    @PostConstruct
    public void setSystemProperties() throws IOException {
        Path path = Paths.get(System.getProperty("user.home"), ".payper", "credentials.properties");
        if (Files.exists(path)) {
            authenticator = PayperAuthenticator.PayperAuthenticators.ofSandBox(path);
        }
    }

    @Bean
    public PayperAuthenticator getPayperAuthenticator() {
        return authenticator;
    }
}

