package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.service.URIService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Profile("local")
public class LocalURIServiceImpl implements URIService {
    @Override
    public URI captureOrder(OrderModel order) {
        return URI.create("http://localhost:8080/webstore.html?action=captureOrder&data=" + order.getReferenceId().value());
    }

    @Override
    public URI cancelOrder(OrderModel order) {
        return URI.create("http://localhost:8080/webstore.html?action=cancelOrder&data=" + order.getReferenceId().value());
    }

    @Override
    public URI authorizeOrder(OrderModel order) {
        return URI.create("http://localhost:8080/webstore.html?action=authorizeOrder&data=" + order.getReferenceId().value());
    }
}
