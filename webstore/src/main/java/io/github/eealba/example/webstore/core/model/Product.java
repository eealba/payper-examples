package io.github.eealba.example.webstore.core.model;

import java.math.BigDecimal;

public record Product(ProductId id,
                      String title,
                      String description,
                      BigDecimal price,
                      String img,
                      PaymentIntent paymentIntent) {
}
