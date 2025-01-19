package com.example;

import io.github.eealba.payper.catalog.products.v1.api.CatalogProductsApiClient;
import io.github.eealba.payper.catalog.products.v1.model.ProductCategory;
import io.github.eealba.payper.catalog.products.v1.model.ProductRequestPOST;

public class App {
    public static void main(String[] args) {
        //Replace the placeholders with your own values
        System.setProperty("PAYPAL-CLIENT-ID", "<PAYPAL-CLIENT-ID>");
        System.setProperty("PAYPAL-CLIENT-SECRET", "<PAYPAL-CLIENT-SECRET>");
        System.setProperty("PAYPAL-BASE-URL", "https://api.sandbox.paypal.com");

        var catalogProductsApiClient = CatalogProductsApiClient.create();

        var productRequest = ProductRequestPOST.builder()
                .name("Product Name")
                .description("Product Description")
                .type(ProductRequestPOST.Type.PHYSICAL)
                .category(ProductCategory.ACCESSORIES)
                .imageUrl("https://example.com/image.jpg")
                .build();

        var product = catalogProductsApiClient.products()
                .create()
                .withBody(productRequest)
                .retrieve()
                .toEntity();

        System.out.println("Created product ID: " + product.id());

    }
}
