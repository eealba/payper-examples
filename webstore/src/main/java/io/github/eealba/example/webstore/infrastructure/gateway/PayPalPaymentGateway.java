package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.SubmitOrderGateway;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.PaymentIntent;
import io.github.eealba.example.webstore.core.service.URIService;
import io.github.eealba.payper.core.client.RequestSpec;
import io.github.eealba.payper.orders.v2.api.CheckoutOrdersApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayPalPaymentGateway implements SubmitOrderGateway {
    private final PayPalMapper payPalMapper;
    private final URIService URIService;
    private final CheckoutOrdersApiClient checkoutOrdersApiClient;


    @Override
    public OrderModel submitOrder(OrderModel orderModel) {
        log.info("Processing payment for order: {}", orderModel);
        Function<OrderModel, URI> uriFunction = orderModel.getPaymentIntent() == PaymentIntent.CAPTURE ?
                URIService::captureOrder : URIService::authorizeOrder;
        var body = payPalMapper.mapOrderRequest(orderModel, uriFunction.apply(orderModel),
                                                URIService.cancelOrder(orderModel));

        var paypalResponse = checkoutOrdersApiClient.orders()
                                                    .create()
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


}
