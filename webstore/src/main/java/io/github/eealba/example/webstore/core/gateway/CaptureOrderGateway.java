package io.github.eealba.example.webstore.core.gateway;
import io.github.eealba.example.webstore.core.model.OrderModel;

public interface CaptureOrderGateway {
    OrderModel captureOrder(OrderModel orderModel);
}
