package io.github.eealba.example.webstore.core.model;

public record OrderRequest(ProductId productId, DeliveryAddress deliveryAddress, PaymentMethod paymentMethod,
                           PaymentIntent paymentIntent) {
}
