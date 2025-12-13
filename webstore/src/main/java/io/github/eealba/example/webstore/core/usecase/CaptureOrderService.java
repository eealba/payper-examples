package io.github.eealba.example.webstore.core.usecase;


import io.github.eealba.example.webstore.core.model.CaptureOrderResponse;
import io.github.eealba.example.webstore.core.model.ReferenceId;

public interface CaptureOrderService {
    CaptureOrderResponse capture(ReferenceId referenceId);
}
