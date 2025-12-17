package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.AuthorizeOrderGateway;
import io.github.eealba.example.webstore.core.model.ExternalAuthorizationId;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderStatus;
import io.github.eealba.payper.core.client.ResponseSpec;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import io.github.eealba.payper.orders.v2.model.ErrorDefault;
import io.github.eealba.payper.orders.v2.model.OrderAuthorizeRequest;
import io.github.eealba.payper.orders.v2.model.OrderAuthorizeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayPalAuthorizeOrderGateway implements AuthorizeOrderGateway {
    private final CheckoutOrdersApiClient checkoutOrdersApiClient;

    @Override
    public OrderModel authorizeOrder(OrderModel orderModel) {
        var response = callPayPal(orderModel);
        return buildResponse(orderModel, response);
    }

    private ResponseSpec.Response<OrderAuthorizeResponse, ErrorDefault> callPayPal(OrderModel orderModel) {
        return checkoutOrdersApiClient
                .orders()
                .authorize()
                .withId(orderModel.getExternalOrderId().value())
                .withBody(OrderAuthorizeRequest.builder().build())
                .retrieve()
                .toResponse();
    }

    private static OrderModel buildResponse(OrderModel orderModel,
                                            ResponseSpec.Response<OrderAuthorizeResponse, ErrorDefault> response) {
        if (!response.isSuccessful()) {
            log.error("Failed to authorize PayPal order: {} - {}", response.statusCode(), response.toRawString());
            throw new RuntimeException("Failed to authorize PayPal order");
        }
        var paypalOrder = response.toEntity();

        log.info("Authorized payment for order: {}", paypalOrder);

        var authorizationId = paypalOrder.purchaseUnits().getFirst().payments().authorizations().getFirst().id();
        return OrderModel.builder(orderModel)
                         .status(OrderStatus.valueOf(paypalOrder.status().name()))
                         .externalAuthorizationId(new ExternalAuthorizationId(authorizationId))
                         .build();
    }
}
