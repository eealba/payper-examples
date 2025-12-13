package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.usecase.CaptureAuthorizationService;
import io.github.eealba.example.webstore.infrastructure.rest.api.PaymentsApi;
import io.github.eealba.example.webstore.infrastructure.rest.model.CaptureAuthorizationResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentsApiImpl implements PaymentsApi {
    private final CaptureAuthorizationService captureAuthorizationService;
    private final WebMapper webMapper;
    @Override
    public ResponseEntity<CaptureAuthorizationResponseDTO> captureAuthorizedPayment(String referenceId) {
        log.info("Capturing authorized payment for referenceId: {}", referenceId);
        var response = captureAuthorizationService.capture(new ReferenceId(referenceId));
        return ResponseEntity.ok(webMapper.toCaptureAuthorizationResponseDTO(response));

    }
}
