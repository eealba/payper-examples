package io.github.eealba.example.webstore.core.service;

import io.github.eealba.example.webstore.core.model.OrderModel;

import java.net.URI;

public interface URIService {
    URI captureOrder(OrderModel order);
    URI cancelOrder(OrderModel order);
    URI authorizeOrder(OrderModel order);
}
