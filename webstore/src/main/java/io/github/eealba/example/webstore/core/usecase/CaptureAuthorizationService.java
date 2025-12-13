package io.github.eealba.example.webstore.core.usecase;


import io.github.eealba.example.webstore.core.model.CaptureAuthorizationResponse;
import io.github.eealba.example.webstore.core.model.ReferenceId;

public interface CaptureAuthorizationService {
    CaptureAuthorizationResponse capture(ReferenceId referenceId);
}
