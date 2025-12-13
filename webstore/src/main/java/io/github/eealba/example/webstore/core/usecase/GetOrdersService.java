package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.model.OrderModel;

import java.util.List;

public interface GetOrdersService {
    List<OrderModel> getOrders();
}
