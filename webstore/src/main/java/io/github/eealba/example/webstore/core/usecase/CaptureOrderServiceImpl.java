package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.gateway.CaptureOrderGateway;
import io.github.eealba.example.webstore.core.model.CaptureOrderResponse;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptureOrderServiceImpl implements CaptureOrderService {
    private final OrderRepository orderRepository;
    private final CaptureOrderGateway captureOrderGateway;
    @Override
    public CaptureOrderResponse capture(ReferenceId referenceId) {
        var order = orderRepository.getOrderByReferenceId(referenceId);
        return order.map(this::executeCapture).map(CaptureOrderResponse::of)
                    .orElse(CaptureOrderResponse.failed(referenceId));
    }

    private OrderModel executeCapture(OrderModel orderModel) {
        var resOrder = captureOrderGateway.captureOrder(orderModel);
        return orderRepository.saveOrder(resOrder);
    }
}
