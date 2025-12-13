package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.model.Product;

import java.util.List;

public interface GetProductsService {
    List<Product> getProducts();
}
