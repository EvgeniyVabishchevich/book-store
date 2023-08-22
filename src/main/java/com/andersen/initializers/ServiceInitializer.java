package com.andersen.initializers;

import com.andersen.repositories.BookRepository;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.services.BookService;
import com.andersen.services.OrderService;
import com.andersen.services.RequestService;
import com.andersen.services.impl.BookServiceImpl;
import com.andersen.services.impl.OrderServiceImpl;
import com.andersen.services.impl.RequestServiceImpl;

public class ServiceInitializer {
    private final BookService bookService;
    private final OrderService orderService;
    private final RequestService requestService;

    public ServiceInitializer(BookRepository bookRepository, OrderRepository orderRepository,
                              RequestRepository requestRepository) {
        bookService = new BookServiceImpl(bookRepository, orderRepository, requestRepository);
        orderService = new OrderServiceImpl(orderRepository, requestRepository);
        requestService = new RequestServiceImpl(requestRepository);
    }

    public BookService getBookService() {
        return bookService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public RequestService getRequestService() {
        return requestService;
    }
}
