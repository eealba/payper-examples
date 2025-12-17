package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.SubmitOrderGateway;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.PaymentIntent;
import io.github.eealba.example.webstore.core.service.URIService;
import io.github.eealba.payper.core.client.RequestSpec;
import io.github.eealba.payper.core.client.ResponseSpec;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import io.github.eealba.payper.orders.v2.model.ErrorDefault;
import io.github.eealba.payper.orders.v2.model.Order;
import io.github.eealba.payper.orders.v2.model.OrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayPalSubmitOrderGateway implements SubmitOrderGateway {
    private final PayPalMapper payPalMapper;
    private final URIService URIService;
    private final CheckoutOrdersApiClient checkoutOrdersApiClient;


    @Override
    public OrderModel submitOrder(OrderModel orderModel) {
        var paypalResponse = callPayPal(orderModel);
        return buildResponse(orderModel, paypalResponse);
    }

    private ResponseSpec.Response<Order, ErrorDefault> callPayPal(OrderModel orderModel) {
        log.info("Processing payment for order: {}", orderModel);
        var body = buildPayPalRequestBody(orderModel);

        return checkoutOrdersApiClient.orders()
                                      .create()
                                      .withPrefer(RequestSpec.Prefer.RETURN_REPRESENTATION)
                                      .withPaypalRequestId(orderModel.getReferenceId().value())
                                      .withBody(body)
                                      .retrieve()
                                      .toFuture()
                                      .join();
    }

    private OrderRequest buildPayPalRequestBody(OrderModel orderModel) {
        var capture = orderModel.getPaymentIntent() == PaymentIntent.CAPTURE;
        Function<OrderModel, URI> uriFunction = capture ? URIService::captureOrder : URIService::authorizeOrder;
        return payPalMapper.mapOrderRequest(orderModel, uriFunction.apply(orderModel),
                                            URIService.cancelOrder(orderModel));
    }

    private OrderModel buildResponse(OrderModel orderModel, ResponseSpec.Response<Order, ErrorDefault> paypalResponse) {
        if (paypalResponse.isSuccessful()) {
            log.info("PayPal order created successfully: {}", paypalResponse.toEntity());
            return payPalMapper.submittedOrder(orderModel, paypalResponse.toEntity());
        } else {
            log.error("Failed to create PayPal order: {} - {}", paypalResponse.statusCode(),
                      paypalResponse.toRawString());
            throw new RuntimeException("Failed to create PayPal order");
        }
    }


}
