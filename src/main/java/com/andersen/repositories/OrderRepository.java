package com.andersen.repositories;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    void save(Order order);

    List<Order> getAllSorted(OrderSortKey sortKey);

    Optional<Order> findById(Long id);

    void changeOrderStatus(Long id, Order.OrderStatus orderStatus);

    void remove(Long id);
}
