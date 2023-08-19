package com.andersen.services.impl;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.services.OrderService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;

@Singleton
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;

    @Inject
    public OrderServiceImpl(OrderRepository orderRepository, RequestRepository requestRepository) {
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Order> getAllSorted(OrderSortKey sortKey) {
        return orderRepository.getAllSorted(sortKey);
    }

    @Override
    public void add(Order order) {
        orderRepository.save(order);
        order.getRequests().forEach(requestRepository::save);//TODO maybe fix
    }

    @Override
    public void complete(Long id) {
        completeRequests(id);

        Order order = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Wrong order id"));

        for (Request request : order.getRequests()) {
            if (request.getRequestStatus() == Request.RequestStatus.IN_PROCESS) {
                order.setStatus(Order.OrderStatus.IN_PROCESS);
                orderRepository.save(order);
                return;
            }
        }

        order.setStatus(Order.OrderStatus.COMPLETED);
        order.setCompletionDate(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void cancel(Long id) {
        orderRepository.remove(id);
    }

    @Override
    public int getIncomeBySpecifiedPeriod(LocalDateTime from, LocalDateTime to) {
        Integer income = 0;

        List<Order> orders = orderRepository.getAllSorted(OrderSortKey.NATURAL);

        for (Order order : orders) {
            if (order.getStatus() == Order.OrderStatus.COMPLETED && order.getCompletionDate().isAfter(from) &&
                    order.getCompletionDate().isBefore(to)) {
                income += order.getPrice();
            }
        }

        return income;
    }

    public void completeRequests(Long orderId) {
        List<Request> requests = requestRepository.findAllByOrderId(orderId);

        for (Request request : requests) {
            if (request.getBook().getStatus() == Book.BookStatus.IN_STOCK) {
                request.setRequestStatus(Request.RequestStatus.COMPLETED);
            } else {
                request.setRequestStatus(Request.RequestStatus.IN_PROCESS);
            }
            requestRepository.save(request);
        }
    }
}
