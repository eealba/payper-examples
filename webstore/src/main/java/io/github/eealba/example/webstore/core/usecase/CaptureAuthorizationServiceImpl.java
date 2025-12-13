package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.gateway.CaptureAuthorizationGateway;
import io.github.eealba.example.webstore.core.model.CaptureAuthorizationResponse;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptureAuthorizationServiceImpl implements CaptureAuthorizationService {
    private final OrderRepository orderRepository;
    private final CaptureAuthorizationGateway captureOrderGateway;

    @Override
    public CaptureAuthorizationResponse capture(ReferenceId referenceId) {
        var order = orderRepository.getOrderByReferenceId(referenceId);

        return order.map(this::executeCapture).orElse(CaptureAuthorizationResponse.failed(referenceId));
    }

    private CaptureAuthorizationResponse executeCapture(OrderModel orderModel) {
        var resOrder = captureOrderGateway.captureAuthorization(orderModel);
        var newOrder = orderRepository.saveOrder(resOrder);
        return CaptureAuthorizationResponse.of(newOrder);
    }
}
