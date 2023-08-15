package com.andersen.repositories.jdbc;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.repositories.OrderRepository;
import com.google.inject.Inject;

import java.util.List;
import java.util.Optional;

public class JdbcOrderRepository implements OrderRepository {

    private final Database database;

    @Inject
    public JdbcOrderRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Order order) {

    }

    @Override
    public List<Order> getAllSorted(OrderSortKey sortKey) {
        return null;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void changeOrderStatus(Long id, Order.OrderStatus orderStatus) {

    }

    @Override
    public void remove(Long id) {

    }
}
