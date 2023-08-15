package com.andersen.services.impl;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;
import com.andersen.repositories.BookRepository;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.services.BookService;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.Objects;

@Singleton
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;

    @Inject
    public BookServiceImpl(BookRepository bookRepository, OrderRepository orderRepository, RequestRepository requestRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Book> getAllSorted(BookSortKey sortKey) {
        return bookRepository.getAllSorted(sortKey);
    }

    @Override
    public List<Book> getAll() {
        return bookRepository.getAll();
    }

    @Override
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public void changeBookStatus(Long id, Book.BookStatus status) {
        bookRepository.changeBookStatus(id, status);

        if (status == Book.BookStatus.IN_STOCK) {
            completeRequests(id);
        }
    }

    public void completeRequests(Long bookId) {
        List<Request> requests = requestRepository.getAll();

        requests.forEach(request -> {
            if (Objects.equals(request.getBook().getId(), bookId)) {
                requestRepository.changeRequestStatus(request.getId(), Request.RequestStatus.COMPLETED);
            }
        });

        completeOrders();
    }

    public void completeOrders() {
        List<Order> orders = orderRepository.getAll();

        for (Order order : orders) {
            completeOrder(order);
        }
    }

    public void completeOrder(Order order) {
        for (Request request : order.getRequests()) {
            if (request.getRequestStatus() == Request.RequestStatus.IN_PROCESS) {
                return;
            }
        }
        orderRepository.changeOrderStatus(order.getId(), Order.OrderStatus.COMPLETED);
    }
}
