package io.github.eealba.example.webstore.core.repository;

import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.ReferenceId;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<OrderModel> getOrders();

    List<OrderModel> getOrdersHistory();

    OrderModel saveOrder(OrderModel orderModel);

    Optional<OrderModel> getOrderByReferenceId(ReferenceId referenceId);
}
