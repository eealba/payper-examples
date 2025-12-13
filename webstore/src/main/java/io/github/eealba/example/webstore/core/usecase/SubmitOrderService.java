package io.github.eealba.example.webstore.core.usecase;


import io.github.eealba.example.webstore.core.model.OrderRequest;
import io.github.eealba.example.webstore.core.model.OrderResponse;

public interface SubmitOrderService {
    OrderResponse submitOrder(OrderRequest request);
}
