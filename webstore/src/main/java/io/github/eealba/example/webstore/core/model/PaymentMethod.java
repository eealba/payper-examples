package io.github.eealba.example.webstore.core.model;

import lombok.Getter;

public record PaymentMethod(TypeEnum type, CardDetails cardDetails) {
    @Getter
    public enum TypeEnum {
        PAYPAL("paypal"),
        CARD("card");
        private final String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public String toString() {
            return String.valueOf(value);
        }

        public static TypeEnum fromValue(String value) {
            for (TypeEnum b : TypeEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

}

