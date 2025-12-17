package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.CaptureOrderGateway;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderStatus;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import io.github.eealba.payper.orders.v2.model.Order;
import io.github.eealba.payper.orders.v2.model.OrderCaptureRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayPalCaptureOrderGateway implements CaptureOrderGateway {
    private final CheckoutOrdersApiClient checkoutOrdersApiClient;

    @Override
    public OrderModel captureOrder(OrderModel orderModel) {
        var paypalOrder = callPayPal(orderModel);
        return buildResponse(orderModel, paypalOrder);
    }

    private Order callPayPal(OrderModel orderModel) {
        var paypalOrder = checkoutOrdersApiClient
                .orders()
                .capture()
                .withId(orderModel.getExternalOrderId().value())
                .withBody(OrderCaptureRequest.builder().build())
                .retrieve()
                .toEntity();

        log.info("Captured payment for order: {}", paypalOrder);
        return paypalOrder;
    }

    private static OrderModel buildResponse(OrderModel orderModel, Order paypalOrder) {
        return OrderModel.builder(orderModel).status(OrderStatus.valueOf(paypalOrder.status().name())).build();
    }

}
