package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.gateway.AuthorizeOrderGateway;
import io.github.eealba.example.webstore.core.model.AuthorizeOrderResponse;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizeOrderServiceImpl implements AuthorizeOrderService {
    private final OrderRepository orderRepository;
    private final AuthorizeOrderGateway authorizeOrderGateway;
    @Override
    public AuthorizeOrderResponse authorize(ReferenceId referenceId) {
        var order = orderRepository.getOrderByReferenceId(referenceId);
        return order.map(this::execute).map(AuthorizeOrderResponse::of)
                    .orElse(AuthorizeOrderResponse.failed(referenceId));
    }

    private OrderModel execute(OrderModel orderModel) {
        var resOrder = authorizeOrderGateway.authorizeOrder(orderModel);
        return orderRepository.saveOrder(resOrder);
    }
}
