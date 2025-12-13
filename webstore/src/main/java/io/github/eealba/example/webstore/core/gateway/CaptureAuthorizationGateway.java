package io.github.eealba.example.webstore.core.gateway;
import io.github.eealba.example.webstore.core.model.OrderModel;

public interface CaptureAuthorizationGateway {
    OrderModel captureAuthorization(OrderModel orderModel);
}
