package io.github.eealba.example.webstore.core.model;

public record ProductId(String value) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ProductId productId = (ProductId) obj;
        return value.equals(productId.value);
    }
}
