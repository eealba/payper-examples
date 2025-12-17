package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.gateway.CaptureAuthorizationGateway;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderStatus;
import io.github.eealba.example.webstore.core.model.PaymentIntent;
import io.github.eealba.payper.payments.v2.api.PaymentsApiClient;
import io.github.eealba.payper.payments.v2.model.Capture2;
import io.github.eealba.payper.payments.v2.model.CaptureRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayPalCaptureAuthorizationGateway implements CaptureAuthorizationGateway {
    private final PaymentsApiClient paymentsApiClient;


    @Override
    public OrderModel captureAuthorization(OrderModel orderModel) {
        var capture2 =
                paymentsApiClient.authorizations().capture()
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
