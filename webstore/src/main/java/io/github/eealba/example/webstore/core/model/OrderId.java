package io.github.eealba.example.webstore.core.model;

public record OrderId(Long value) implements Comparable<OrderId> {
    @Override
    public int compareTo(OrderId o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        OrderId orderId = (OrderId) obj;
        return value.equals(orderId.value);
    }
}
