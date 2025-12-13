package io.github.eealba.example.webstore.core.model;

public record CaptureOrderResponse(
        ReferenceId referenceId,
        OrderStatus status) {

    public static CaptureOrderResponse failed(ReferenceId referenceId) {
        return new CaptureOrderResponse(referenceId, OrderStatus.FAILED);
    }

    public static CaptureOrderResponse of(OrderModel orderModel) {
        return new CaptureOrderResponse(orderModel.getReferenceId(), orderModel.getStatus());
    }
}
