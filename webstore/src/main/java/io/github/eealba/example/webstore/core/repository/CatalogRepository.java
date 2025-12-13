package io.github.eealba.example.webstore.core.repository;

import io.github.eealba.example.webstore.core.model.Product;
import io.github.eealba.example.webstore.core.model.ProductId;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository {
    List<Product> getProducts();
    Optional<Product> getProductById(ProductId productId);
}
