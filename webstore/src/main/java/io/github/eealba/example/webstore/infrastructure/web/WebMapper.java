package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.model.AuthorizeOrderResponse;
import io.github.eealba.example.webstore.core.model.CaptureAuthorizationResponse;
import io.github.eealba.example.webstore.core.model.CaptureOrderResponse;
import io.github.eealba.example.webstore.core.model.ExternalOrderId;
import io.github.eealba.example.webstore.core.model.OrderId;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderRequest;
import io.github.eealba.example.webstore.core.model.OrderResponse;
import io.github.eealba.example.webstore.core.model.ExternalAuthorizationId;
import io.github.eealba.example.webstore.core.model.Product;
import io.github.eealba.example.webstore.core.model.ProductId;
import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.infrastructure.rest.model.AuthorizeOrderResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.CaptureAuthorizationResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.CaptureOrderResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderModelDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderRequestDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.OrderResponseDTO;
import io.github.eealba.example.webstore.infrastructure.rest.model.ProductDTO;
import org.mapstruct.Mapper;

import java.net.URI;
import java.util.List;

@Mapper(componentModel = "spring")
public interface WebMapper {
    List<ProductDTO> toProductListDTO(List<Product> catalogs);

    OrderRequest toCheckoutRequest(OrderRequestDTO value);

    OrderResponseDTO toCheckoutResponseDTO(OrderResponse value);

    CaptureOrderResponseDTO toCaptureOrderResponseDTO(CaptureOrderResponse response);

    default String productIdToString(ProductId id) {
        return id == null ? null : id.value();
    }

    default ProductId stringToProductId(String value) {
        return value == null ? null : new ProductId(value);
    }

    default String orderIdToString(OrderId id) {
        return id == null ? null : String.valueOf(id.value());
    }

    default OrderId stringToOrderId(String value) {
        return value == null ? null : new OrderId(Long.valueOf(value));
    }

    default String URIToString(URI uri) {
        return uri == null ? null : String.valueOf(uri.toString());
    }

    default String externalOrderIdToString(ExternalOrderId id) {
        return id == null ? null : id.value();
    }

    default ExternalOrderId stringToExternalOrderId(String value) {
        return value == null ? null : new ExternalOrderId(value);
    }
    default String externalAuthorizationIdToString(ExternalAuthorizationId id) {
        return id == null ? null : id.value();
    }

    default ExternalAuthorizationId stringToExternalAuthorizationId(String value) {
        return value == null ? null : new ExternalAuthorizationId(value);
    }

    default String referenceIdToString(ReferenceId id) {
        return id == null ? null : id.value();
    }

    default ReferenceId stringToReferenceId(String value) {
        return value == null ? null : new ReferenceId(value);
    }


    List<OrderModelDTO> toOrderModelListDTO(List<OrderModel> orders);

    AuthorizeOrderResponseDTO toAuthorizeOrderResponseDTO(AuthorizeOrderResponse response);

    CaptureAuthorizationResponseDTO toCaptureAuthorizationResponseDTO(CaptureAuthorizationResponse response);
}
