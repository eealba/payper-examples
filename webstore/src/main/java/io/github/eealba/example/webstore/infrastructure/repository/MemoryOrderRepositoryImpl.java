package io.github.eealba.example.webstore.infrastructure.repository;

import io.github.eealba.example.webstore.core.model.OrderModel;
import io.github.eealba.example.webstore.core.model.OrderId;
import io.github.eealba.example.webstore.core.model.ReferenceId;
import io.github.eealba.example.webstore.core.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
public class MemoryOrderRepositoryImpl implements OrderRepository {
    private final Map<OrderId, OrderModel> orders = new TreeMap<>();
    private final AtomicLong counter = new AtomicLong();
    private final Deque<OrderModel> history = new ArrayDeque<>();

    @Override
    public List<OrderModel> getOrders() {
        log.info("Get all orders");
        var res = new ArrayList<>(List.of(orders.values().toArray(new OrderModel[0])));
        log.info("Found {} orders", res.size());
        res.sort((o1, o2) -> o2.getId().compareTo(o1.getId())); // most recent first
        return res;
    }

    @Override
    public List<OrderModel> getOrdersHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }


    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        log.info("Saving order {}", orderModel);
        if (orderModel.getId() == null) {
            orderModel = OrderModel.builder(orderModel).id(new OrderId(counter.incrementAndGet())).build();
        }
        orders.put(orderModel.getId(), orderModel);
        // push the saved order onto the history stack (most recent first)
        synchronized (history) {
            history.addFirst(orderModel);
        }
        return orderModel;
    }

    @Override
    public Optional<OrderModel> getOrderByReferenceId(ReferenceId referenceId) {
        log.info("Get order by reference id {}", referenceId);
         for (OrderModel order : orders.values()) {
             if (order.getReferenceId().equals(referenceId)) {
                 return Optional.of(order);
             }
         }
        return Optional.empty();
    }
}
