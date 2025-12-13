package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.AuthorizeOrderGateway;
import io.github.eealba.example.webstore.core.gateway.CaptureAuthorizationGateway;
import io.github.eealba.example.webstore.core.gateway.CaptureOrderGateway;
import io.github.eealba.example.webstore.core.gateway.SubmitOrderGateway;
import io.github.eealba.example.webstore.core.model.ExternalAuthorizationId;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderStatus;
import io.github.eealba.example.webstore.core.model.PaymentIntent;
import io.github.eealba.example.webstore.core.service.URIService;
import io.github.eealba.payper.core.client.PayperConfig;
import io.github.eealba.payper.core.client.RequestSpec;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import io.github.eealba.payper.orders.v2.api.Orders;
import io.github.eealba.payper.orders.v2.model.OrderAuthorizeRequest;
import io.github.eealba.payper.orders.v2.model.OrderCaptureRequest;
import io.github.eealba.payper.payments.v2.api.Authorizations;
import io.github.eealba.payper.payments.v2.api.PaymentsApiClient;
import io.github.eealba.payper.payments.v2.model.Capture2;
import io.github.eealba.payper.payments.v2.model.CaptureRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Function;

@Slf4j
@Component
public class PayPalPaymentGateway implements AuthorizeOrderGateway, CaptureOrderGateway, SubmitOrderGateway,
                                             CaptureAuthorizationGateway {
    private final Orders orders;
    private final Authorizations authorizations;
    private final PayPalMapper payPalMapper;
    private final URIService URIService;

    public PayPalPaymentGateway(PayperConfig payperConfig, PayPalMapper payPalMapper, URIService URIService) {
        orders = CheckoutOrdersApiClient.create(payperConfig).orders();
        authorizations = PaymentsApiClient.create(payperConfig).authorizations();
        this.payPalMapper = payPalMapper;
        this.URIService = URIService;
    }


    @Override
    public OrderModel submitOrder(OrderModel orderModel) {
        log.info("Processing payment for order: {}", orderModel);
        Function<OrderModel, URI> uriFunction = orderModel.getPaymentIntent() == PaymentIntent.CAPTURE ?
                URIService::captureOrder : URIService::authorizeOrder;
        var body = payPalMapper.mapOrderRequest(orderModel, uriFunction.apply(orderModel),
                                                URIService.cancelOrder(orderModel));

        var paypalResponse =
                orders.create()
                      .withPrefer(RequestSpec.Prefer.RETURN_REPRESENTATION)
                      .withPaypalRequestId(orderModel.getReferenceId().value())
                      .withBody(body)
                      .retrieve()
                      .toResponse();

        if (paypalResponse.isSuccessful()) {
            log.info("PayPal order created successfully: {}", paypalResponse.toEntity());
            return payPalMapper.submittedOrder(orderModel, paypalResponse.toEntity());
        } else {
            log.error("Failed to create PayPal order: {} - {}", paypalResponse.statusCode(),
                      paypalResponse.toRawString());
            throw new RuntimeException("Failed to create PayPal order");
        }
    }


    @Override
    public OrderModel captureOrder(OrderModel orderModel) {
        var paypalOrder =
                orders.capture()
                      .withId(orderModel.getExternalOrderId().value())
                      .withBody(OrderCaptureRequest.builder().build())
                      .retrieve()
                      .toEntity();

        log.info("Captured payment for order: {}", paypalOrder);

        return OrderModel.builder(orderModel).status(OrderStatus.valueOf(paypalOrder.status().name())).build();

    }

    @Override
    public OrderModel authorizeOrder(OrderModel orderModel) {
        var response = orders.authorize()
                             .withId(orderModel.getExternalOrderId().value())
                             .withBody(OrderAuthorizeRequest.builder().build())
                             .retrieve()
                             .toResponse();
        if (!response.isSuccessful()) {
            log.error("Failed to authorize PayPal order: {} - {}", response.statusCode(),
                      response.toRawString());
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

    @Override
    public OrderModel captureAuthorization(OrderModel orderModel) {
        var capture2 =
                authorizations.capture()
                              .withId(orderModel.getExternalAuthorizationId().value())
                              .withBody(CaptureRequest.builder().build())
                              .retrieve()
                              .toEntity();

        log.info("Captured authorization for order: {} capture: {}", orderModel, capture2);
        var completed = capture2.status() == Capture2.Status.COMPLETED;
        return OrderModel.builder(orderModel)
                         .status(completed ? OrderStatus.COMPLETED : OrderStatus.FAILED)
                         .paymentIntent(completed ? PaymentIntent.CAPTURE: orderModel.getPaymentIntent())
                         .build();
    }
}
