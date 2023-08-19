package com.andersen.repositories.local;

import com.andersen.config.model.ConfigModel;
import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class LocalOrderRepository implements OrderRepository {
    private final ObjectMapper objectMapper;
    private final ConfigModel configModel;

    @Inject
    public LocalOrderRepository(ObjectMapper objectMapper, ConfigModel configModel) {
        this.objectMapper = objectMapper;
        this.configModel = configModel;
    }

    @Override
    public List<Order> getAllSorted(OrderSortKey sortKey) {
        try {
            List<Order> orders = objectMapper.readValue(new File(configModel.savePath()), AppState.class).getOrders();

            switch (sortKey) {
                case PRICE -> orders.sort(Comparator.comparing(Order::getPrice));
                case STATUS -> orders.sort(Comparator.comparing(Order::getStatus));
                case DATE -> orders.sort(Comparator.comparing(Order::getCompletionDate));
            }

            return orders;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        return getAllSorted(OrderSortKey.NATURAL).stream().filter(order -> Objects.equals(order.getId(), id)).findFirst();
    }

    @Override
    public void remove(Long id) {
        List<Order> orders = getAllSorted(OrderSortKey.NATURAL);

        Order searchedOrder = findById(orders, id);

        orders.remove(searchedOrder);

        save(orders);
    }

    public Order findById(List<Order> orders, Long id) {
        for (Order order : orders) {
            if (Objects.equals(order.getId(), id)) {
                return order;
            }
        }
        throw new IllegalArgumentException("Wrong books id");
    }

    @Override
    public void save(Order order) {
        List<Order> orders = getAllSorted(OrderSortKey.NATURAL);

        orders.add(order);

        save(orders);
    }

    public void save(List<Order> orders) {
        try {
            AppState appState = objectMapper.readValue(new File(configModel.savePath()), AppState.class);

            appState.synchronizeOrders(orders);

            objectMapper.writeValue(new File(configModel.savePath()), appState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
