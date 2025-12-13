package io.github.eealba.example.webstore.core.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public final class OrderModel {
    private final Product product;
    private final DeliveryAddress deliveryAddress;
    private final PaymentMethod paymentMethod;
    private final PaymentIntent paymentIntent;
    private final Instant createTime;
    private final Instant updateTime;
    private final OrderId id;
    private final ExternalOrderId externalOrderId;
    private final PaymentProvider paymentProvider;
    private final OrderStatus status;
    private final List<LinkDescription> links;
    private final ReferenceId referenceId;
    private final ExternalAuthorizationId externalAuthorizationId;


    private OrderModel(Builder builder) {
        this.product = builder.product;
        this.deliveryAddress = builder.deliveryAddress;
        this.paymentMethod = builder.paymentMethod;
        this.createTime = builder.createTime;
        this.updateTime = builder.updateTime;
        this.id = builder.id;
        this.externalOrderId = builder.externalOrderId;
        this.paymentProvider = builder.paymentProvider;
        this.status = builder.status;
        this.links = builder.links == null ? Collections.emptyList() : List.copyOf(builder.links);
        this.referenceId = builder.referenceId == null ? new ReferenceId(java.util.UUID.randomUUID().toString()) :
                builder.referenceId;
        this.paymentIntent = builder.paymentIntent;
        this.externalAuthorizationId = builder.externalAuthorizationId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(OrderModel orderModel) {
        return new Builder(orderModel);
    }

    public static class Builder {
        public PaymentIntent paymentIntent;
        public ExternalAuthorizationId externalAuthorizationId;
        private Product product;
        private DeliveryAddress deliveryAddress;
        private PaymentMethod paymentMethod;
        private Instant createTime;
        private Instant updateTime;
        private OrderId id;
        private ExternalOrderId externalOrderId;
        private PaymentProvider paymentProvider;
        private OrderStatus status;
        private List<LinkDescription> links;
        private ReferenceId referenceId;

        // No-args constructor
        public Builder() {
        }

        // Constructor that copies values from an existing Order
        public Builder(OrderModel orderModel) {
            this.product = orderModel.getProduct();
            this.deliveryAddress = orderModel.getDeliveryAddress();
            this.paymentMethod = orderModel.getPaymentMethod();
            this.createTime = orderModel.getCreateTime();
            this.updateTime = orderModel.getUpdateTime();
            this.id = orderModel.getId();
            this.externalOrderId = orderModel.getExternalOrderId();
            this.externalAuthorizationId = orderModel.getExternalAuthorizationId();
            this.paymentProvider = orderModel.getPaymentProvider();
            this.status = orderModel.getStatus();
            this.links = orderModel.getLinks();
            this.referenceId = orderModel.getReferenceId();
            this.paymentIntent = orderModel.getPaymentIntent();
        }

        public Builder product(Product product) {
            this.product = product;
            return this;
        }

        public Builder deliveryAddress(DeliveryAddress deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public Builder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder createTime(Instant createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder updateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder id(OrderId id) {
            this.id = id;
            return this;
        }

        public Builder paymentProviderId(ExternalOrderId externalOrderId) {
            this.externalOrderId = externalOrderId;
            return this;
        }

        public Builder paymentProvider(PaymentProvider paymentProvider) {
            this.paymentProvider = paymentProvider;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder links(List<LinkDescription> links) {
            this.links = links;
            return this;
        }

        public Builder referenceId(ReferenceId referenceId) {
            this.referenceId = referenceId;
            return this;
        }
        public Builder paymentIntent(PaymentIntent paymentIntent) {
            this.paymentIntent = paymentIntent;
            return this;
        }

        public Builder externalAuthorizationId(ExternalAuthorizationId externalAuthorizationId) {
            this.externalAuthorizationId = externalAuthorizationId;
            return this;
        }

        public OrderModel build() {
            Objects.requireNonNull(product, "product");
            Objects.requireNonNull(deliveryAddress, "deliveryAddress");
            Objects.requireNonNull(paymentMethod, "paymentMethod");
            return new OrderModel(this);
        }
    }
}
