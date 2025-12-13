package io.github.eealba.example.webstore.core.usecase;


import io.github.eealba.example.webstore.core.model.AuthorizeOrderResponse;
import io.github.eealba.example.webstore.core.model.ReferenceId;

public interface AuthorizeOrderService {
    AuthorizeOrderResponse authorize(ReferenceId referenceId);
}
