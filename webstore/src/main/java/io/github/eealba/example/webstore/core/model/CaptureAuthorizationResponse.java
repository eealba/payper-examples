package io.github.eealba.example.webstore.core.model;

public record CaptureAuthorizationResponse(
        ReferenceId referenceId,
        AuthorizationStatus status) {

    public static CaptureAuthorizationResponse failed(ReferenceId referenceId) {
        return new CaptureAuthorizationResponse(referenceId, AuthorizationStatus.FAILED);
    }

    public static CaptureAuthorizationResponse of(OrderModel orderModel) {
        return new CaptureAuthorizationResponse(orderModel.getReferenceId(),
                                                orderModel.getStatus() == OrderStatus.COMPLETED ?
                                                        AuthorizationStatus.COMPLETED :
                                                        AuthorizationStatus.FAILED);
    }
}
