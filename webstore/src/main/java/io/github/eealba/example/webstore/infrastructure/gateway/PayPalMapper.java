package io.github.eealba.example.webstore.infrastructure.gateway;

import io.github.eealba.example.webstore.core.model.ExternalAuthorizationId;
import io.github.eealba.example.webstore.core.model.ExternalOrderId;
import io.github.eealba.example.webstore.core.model.LinkDescription;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderStatus;
import io.github.eealba.example.webstore.core.model.PaymentMethod;
import io.github.eealba.example.webstore.core.model.PaymentProvider;
import io.github.eealba.payper.orders.v2.model.AmountWithBreakdown;
import io.github.eealba.payper.orders.v2.model.CardRequest;
import io.github.eealba.payper.orders.v2.model.CheckoutPaymentIntent;
import io.github.eealba.payper.orders.v2.model.CurrencyCode;
import io.github.eealba.payper.orders.v2.model.DateYearMonth;
import io.github.eealba.payper.orders.v2.model.Language;
import io.github.eealba.payper.orders.v2.model.Order;
import io.github.eealba.payper.orders.v2.model.OrderRequest;
import io.github.eealba.payper.orders.v2.model.PaymentSource;
import io.github.eealba.payper.orders.v2.model.PaypalWallet;
import io.github.eealba.payper.orders.v2.model.PaypalWalletExperienceContext;
import io.github.eealba.payper.orders.v2.model.PurchaseUnitRequest;
import io.github.eealba.payper.orders.v2.model.Url;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class PayPalMapper {
    public OrderRequest mapOrderRequest(OrderModel orderModel,
                                        URI captureUri,
                                        URI cancelUri) {
        var intent = switch (orderModel.getPaymentIntent()) {
            case AUTHORIZE -> CheckoutPaymentIntent.AUTHORIZE;
            default -> CheckoutPaymentIntent.CAPTURE;
        };

        var paymentSource = orderModel.getPaymentMethod().type() == PaymentMethod.TypeEnum.PAYPAL ?
                paypalPaymentSource(captureUri, cancelUri) : cardPaymentSource(orderModel);

        return OrderRequest.builder()
                           .intent(intent)
                           .purchaseUnits(List.of(getPurchaseUnitRequest(orderModel)))
                           .paymentSource(paymentSource)
                           .build();
    }

    private static PurchaseUnitRequest getPurchaseUnitRequest(OrderModel orderModel) {
        var amount = AmountWithBreakdown.builder()
                                        .value(orderModel.getProduct().price().toPlainString())
                                        .currencyCode(CurrencyCode.EUR)
                                        .build();
        return PurchaseUnitRequest.builder().referenceId(orderModel.getReferenceId().value()).amount(amount).build();
    }

    private PaymentSource paypalPaymentSource(URI captureUri,
                                              URI cancelUri) {
        var experienceContext = PaypalWalletExperienceContext.builder()
                                                             .brandName("EXAMPLE INC")
                                                             .locale(new Language(("es-ES")))
                                                             .landingPage(PaypalWalletExperienceContext.LandingPage.LOGIN)
                                                             .userAction(PaypalWalletExperienceContext.UserAction.PAY_NOW)
                                                             .returnUrl(new Url(captureUri.toString()))
                                                             .cancelUrl(new Url(cancelUri.toString()))
                                                             .build();
        return PaymentSource.builder()
                            .paypal(PaypalWallet.builder().experienceContext(experienceContext).build())
                            .build();
    }

    private PaymentSource cardPaymentSource(OrderModel orderModel) {
        var cardDetails = orderModel.getPaymentMethod().cardDetails();
        return PaymentSource.builder()
                            .card(CardRequest.builder()
                                             .number(cardDetails.cardNumber())
                                             .expiry(new DateYearMonth(cardDetails.expiration()))
                                             .securityCode(cardDetails.cardCvc())
                                             .name(cardDetails.cardName())
                                             .build())

                            .build();
    }

    public OrderModel submittedOrder(OrderModel orderModel, io.github.eealba.payper.orders.v2.model.Order paypalOrder) {
        var links = new ArrayList<LinkDescription>();
        for (var link : paypalOrder.links()) {
            links.add(new LinkDescription(link.href(), link.rel()));
        }
        ExternalAuthorizationId externalAuthorizationId = getExternalAuthorizationId(paypalOrder);

        return OrderModel.builder(orderModel)
                         .createTime(paypalOrder.createTime() == null ? Instant.now() : paypalOrder.createTime())
                         .updateTime(paypalOrder.updateTime())
                         .paymentProviderId(new ExternalOrderId(paypalOrder.id()))
                         .paymentProvider(PaymentProvider.PAYPAL)
                         .externalAuthorizationId(externalAuthorizationId)
                         .status(OrderStatus.valueOf(paypalOrder.status().name()))
                         .links(links).build();


    }

    private static ExternalAuthorizationId getExternalAuthorizationId(Order paypalOrder) {
        ExternalAuthorizationId externalAuthorizationId = null;
        if (paypalOrder.purchaseUnits() != null && !paypalOrder.purchaseUnits().isEmpty()) {
            var firstUnit = paypalOrder.purchaseUnits().getFirst();
            if (firstUnit.payments() != null && firstUnit.payments().authorizations() != null &&
                    !firstUnit.payments().authorizations().isEmpty()) {
                externalAuthorizationId =
                        new ExternalAuthorizationId(firstUnit.payments().authorizations().getFirst().id());
            }
        }
        return externalAuthorizationId;
    }
}
