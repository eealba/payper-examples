package io.github.eealba.example.webstore.core.gateway;
import io.github.eealba.example.webstore.core.model.OrderModel;

public interface AuthorizeOrderGateway {
    OrderModel authorizeOrder(OrderModel orderModel);
}
