package com.andersen.services;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<Order> getAllSorted(OrderSortKey sortKey);

    void add(Order order);

    void complete(Long id);

    void cancel(Long id);

    int getIncomeBySpecifiedPeriod(LocalDateTime from, LocalDateTime to);
}
