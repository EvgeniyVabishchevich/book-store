package com.andersen.services.impl;

import com.andersen.enums.BookSortKey;
import com.andersen.enums.OrderSortKey;
import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;
import com.andersen.repositories.BookRepository;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.RequestRepository;
import com.andersen.services.BookService;

import java.time.LocalDateTime;
import java.util.List;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;

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
    public Book findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public void changeBookStatus(Long id, Book.BookStatus status) {
        Book book = bookRepository.findById(id);
        book.setStatus(status);
        bookRepository.save(book);

        if (status == Book.BookStatus.IN_STOCK) {
            completeRequests(id);
            completeOrders();
        }
    }

    public void completeRequests(Long bookId) {
        List<Request> requests = requestRepository.findAllByBookId(bookId);

        requests.forEach(request -> {
            if (request.getRequestStatus() == Request.RequestStatus.IN_PROCESS) {
                request.setRequestStatus(Request.RequestStatus.COMPLETED);
                requestRepository.save(request);
            }
        });
    }

    public void completeOrders() {
        List<Order> orders = orderRepository.getAllSorted(OrderSortKey.NATURAL);

        for (Order order : orders) {
            if (order.getStatus() == Order.OrderStatus.IN_PROCESS) {
                completeOrder(order);
            }
        }
    }

    public void completeOrder(Order order) {
        for (Request request : order.getRequests()) {
            if (request.getRequestStatus() == Request.RequestStatus.IN_PROCESS) {
                return;
            }
        }
        order.setStatus(Order.OrderStatus.COMPLETED);
        order.setCompletionDate(LocalDateTime.now());
        orderRepository.save(order);
    }
}
