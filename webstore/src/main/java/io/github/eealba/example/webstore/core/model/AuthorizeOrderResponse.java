package io.github.eealba.example.webstore.core.model;

public record AuthorizeOrderResponse(
        ReferenceId referenceId,
        OrderStatus status) {

    public static AuthorizeOrderResponse failed(ReferenceId referenceId) {
        return new AuthorizeOrderResponse(referenceId, OrderStatus.FAILED);
    }

    public static AuthorizeOrderResponse of(OrderModel orderModel) {
        return new AuthorizeOrderResponse(orderModel.getReferenceId(), orderModel.getStatus());
    }
}
