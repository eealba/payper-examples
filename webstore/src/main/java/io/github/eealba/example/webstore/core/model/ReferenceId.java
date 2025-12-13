package io.github.eealba.example.webstore.core.model;

public record ReferenceId(String value) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ReferenceId that = (ReferenceId) obj;
        return value.equalsIgnoreCase(that.value);
    }
}
