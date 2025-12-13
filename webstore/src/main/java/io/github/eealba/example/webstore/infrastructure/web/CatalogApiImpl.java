package io.github.eealba.example.webstore.infrastructure.web;

import io.github.eealba.example.webstore.core.usecase.GetProductsService;
import io.github.eealba.example.webstore.infrastructure.rest.api.CatalogApi;
import io.github.eealba.example.webstore.infrastructure.rest.model.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CatalogApiImpl implements CatalogApi {
    private final GetProductsService getProductsService;
    private final WebMapper webMapper;

    @Override
    public ResponseEntity<List<ProductDTO>> getCatalog() {
        return ResponseEntity.ok(webMapper.toProductListDTO(getProductsService.getProducts()));
    }
}
