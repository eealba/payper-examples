package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.usecase.AuthorizeOrderService;
import io.github.eealba.example.webstore.core.usecase.CaptureOrderService;
import io.github.eealba.example.webstore.core.usecase.SubmitOrderService;
import io.github.eealba.example.webstore.infrastructure.rest.api.CheckoutApi;
import io.github.eealba.example.webstore.infrastructure.rest.model.AuthorizeOrderResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.CaptureOrderResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderRequestDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CheckoutApiImpl implements CheckoutApi {
    private final SubmitOrderService submitOrderService;
    private final CaptureOrderService captureOrderService;
    private final AuthorizeOrderService authorizeOrderService;
    private final WebMapper webMapper;

    @Override
    public ResponseEntity<OrderResponseDTO> submitOrder(OrderRequestDTO requestDTO) {
        log.info("Submitting order: {}", requestDTO);
        var request = webMapper.toCheckoutRequest(requestDTO);
        var response = submitOrderService.submitOrder(request);
        return ResponseEntity.ok(webMapper.toCheckoutResponseDTO(response));
    }


    @Override
    public ResponseEntity<CaptureOrderResponseDTO> captureOrderPayment(String referenceId) {
        log.info("Capturing order payment for referenceId: {}", referenceId);
        var response = captureOrderService.capture(new ReferenceId(referenceId));
        return ResponseEntity.ok(webMapper.toCaptureOrderResponseDTO(response));
    }

    @Override
    public ResponseEntity<AuthorizeOrderResponseDTO> authorizeOrderPayment(String referenceId) {
        log.info("Authorizing order payment for referenceId: {}", referenceId);
        var response = authorizeOrderService.authorize(new ReferenceId(referenceId));
        return ResponseEntity.ok(webMapper.toAuthorizeOrderResponseDTO(response));
    }

}
