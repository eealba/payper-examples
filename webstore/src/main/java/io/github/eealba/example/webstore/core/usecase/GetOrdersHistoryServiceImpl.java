package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersHistoryServiceImpl implements GetOrdersHistoryService {
    private final OrderRepository orderRepository;

    @Override
    public List<OrderModel> getOrdersHistory() {
        return orderRepository.getOrdersHistory();
    }
}

