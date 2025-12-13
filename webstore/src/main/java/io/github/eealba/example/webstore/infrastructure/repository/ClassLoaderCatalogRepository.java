package io.github.eealba.example.webstore.infrastructure.repository;

import io.github.eealba.example.webstore.core.model.Product;
import io.github.eealba.example.webstore.core.model.ProductId;
import io.github.eealba.example.webstore.core.repository.CatalogRepository;
import io.github.eealba.jasoner.JasonerBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ClassLoaderCatalogRepository implements CatalogRepository {
    private final List<Product> productList;

    public ClassLoaderCatalogRepository() throws IOException {
        try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/products.json")) {
            if (inputStream == null) {
                throw new IllegalStateException("products.json not found");
            }
            String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            var tipo = new ArrayList<Product>() {}.getClass().getGenericSuperclass();
            this.productList = JasonerBuilder.create().fromJson(json, tipo);
        }

    }

    @Override
    public List<Product> getProducts() {
        return productList;
    }

    @Override
    public Optional<Product> getProductById(ProductId productId) {
        return productList.stream()
                          .filter(p -> p.id().equals(productId))
                          .findFirst();
    }
}
