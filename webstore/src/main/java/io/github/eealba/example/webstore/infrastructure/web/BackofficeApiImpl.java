package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.usecase.GetOrdersHistoryService;
import io.github.eealba.example.webstore.core.usecase.GetOrdersService;
import io.github.eealba.example.webstore.infrastructure.rest.api.BackofficeApi;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderModelDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
public class BackofficeApiImpl implements BackofficeApi {
    private final WebMapper webMapper;
    private final GetOrdersService getOrdersService;
    private final GetOrdersHistoryService getOrdersHistoryService;
    @Override
    public ResponseEntity<List<OrderModelDTO>> getBackofficeOrders() {
        var orders = getOrdersService.getOrders();
        var ordersDTO = webMapper.toOrderModelListDTO(orders);
        return ResponseEntity.ok(ordersDTO);
    }

    @Override
    public ResponseEntity<List<OrderModelDTO>> getOrderHistory() {
        var orders = getOrdersHistoryService.getOrdersHistory();
        var ordersDTO = webMapper.toOrderModelListDTO(orders);
        return ResponseEntity.ok(ordersDTO);
    }
}
