package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.model.Product;
import io.github.eealba.example.webstore.core.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetProductsServiceImpl implements GetProductsService {
    private final CatalogRepository catalogRepository;

    @Override
    public List<Product> getProducts() {
        return catalogRepository.getProducts();
    }


}
