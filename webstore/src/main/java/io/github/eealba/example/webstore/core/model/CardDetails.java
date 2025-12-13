package io.github.eealba.example.webstore.core.model;

public record CardDetails(String cardNumber, String cardName, String cardExpiry, String cardCvc) {
    public String expirationYear() {
        return "20" + cardExpiry.split("/")[1];
    }

    public String expirationMonth() {
        return cardExpiry.split("/")[0];
    }
    public String expiration() {
        return expirationYear() + "-" + expirationMonth();
    }
}

