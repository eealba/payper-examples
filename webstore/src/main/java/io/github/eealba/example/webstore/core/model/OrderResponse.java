package io.github.eealba.example.webstore.core.model;

import java.net.URI;

public record OrderResponse(OrderStatus status, OrderId id, URI approvalUrl) {
}
