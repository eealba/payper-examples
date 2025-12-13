package io.github.eealba.example.webstore.core.usecase;

import io.github.eealba.example.webstore.core.gateway.SubmitOrderGateway;
import io.github.eealba.example.webstore.core.model.LinkDescription;
import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderRequest;
import io.github.eealba.example.webstore.core.model.OrderResponse;
import io.github.eealba.example.webstore.core.repository.CatalogRepository;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitOrderServiceImpl implements SubmitOrderService {
    private final SubmitOrderGateway submitOrderGateway;
    private final CatalogRepository catalogRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse submitOrder(OrderRequest request) {
        log.info("Processing checkout: {}", request);
        var order = createOrder(request);


        var newOrder = submitOrderGateway.submitOrder(order);

        orderRepository.saveOrder(newOrder);
        log.info("Order processed: {}", newOrder);
        return new OrderResponse(newOrder.getStatus(), newOrder.getId(), getApprovalUri(newOrder.getLinks()));
    }

    private OrderModel createOrder(OrderRequest request) {
        var product = catalogRepository.getProductById(request.productId()).orElseThrow();
        var order = OrderModel.builder()
                              .product(product)
                              .deliveryAddress(request.deliveryAddress())
                              .paymentMethod(request.paymentMethod())
                              .paymentIntent(request.paymentIntent())
                              .build();
        log.info("Checking order: {}", order);
        return order;
    }

    private URI getApprovalUri(List<LinkDescription> links) {
        return links.stream().filter(linkDescription -> "payer-action".equals(linkDescription.rel()))
                    .findFirst()
                    .map(LinkDescription::href)
                    .map(URI::create)
                    .orElse(null);

    }

}
