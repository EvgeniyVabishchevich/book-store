package com.andersen.repositories.local;

import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppState {
    private List<Book> books;
    private List<Order> orders;

    public AppState() {
    }

    public List<Book> getBooks() {
        return books;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Request> getRequests() {
        List<Request> requests = new ArrayList<>();
        orders.forEach(order -> requests.addAll(order.getRequests()));

        return requests;
    }

    public void synchronizeBooks(List<Book> newBooks) {
        for (Book newBook : newBooks) {

            Book matchingBook = findIdMatchingBook(newBook, books);

            if (matchingBook == null) {
                books.add(newBook);
            } else if (!matchingBook.equals(newBook)) {
                matchingBook.setName(newBook.getName());
                matchingBook.setStatus(newBook.getStatus());
                matchingBook.setPrice(newBook.getPrice());
            }
        }

        if (newBooks.size() != books.size()) {
            for (Book oldBook : books) {
                Book matchingBook = findIdMatchingBook(oldBook, newBooks);

                if (matchingBook == null) {
                    books.remove(oldBook);
                }
            }
        }
    }

    public Book findIdMatchingBook(Book currentBook, List<Book> books) {
        for (Book book : books) {
            if (Objects.equals(currentBook.getId(), book.getId())) {
                return book;
            }
        }
        return null;
    }

    public void synchronizeOrders(List<Order> newOrders) {
        for (Order newOrder : newOrders) {

            Order matchingOrder = findIdMatchingOrder(newOrder, orders);

            if (matchingOrder == null) {
                orders.add(newOrder);
            } else if (!matchingOrder.equals(newOrder)) {
                matchingOrder.setCompletionDate(newOrder.getCompletionDate());
                matchingOrder.setStatus(newOrder.getStatus());
                matchingOrder.setPrice(newOrder.getPrice());
                matchingOrder.setRequests(newOrder.getRequests());
                matchingOrder.setClientId(newOrder.getClientId());
            }
        }

        if (newOrders.size() != orders.size()) {

            List<Order> oldOrders = new ArrayList<>();

            for (Order oldOrder : orders) {
                Order matchingOrder = findIdMatchingOrder(oldOrder, newOrders);

                if (matchingOrder == null) {
                    oldOrders.add(oldOrder);
                }
            }

            oldOrders.forEach(oldOrder -> orders.remove(oldOrder));
        }

        List<Request> newRequests = new ArrayList<>();
        orders.forEach(order -> newRequests.addAll(order.getRequests()));

        synchronizeRequests(newRequests);
    }

    public Order findIdMatchingOrder(Order currentOrder, List<Order> orders) {
        for (Order order : orders) {
            if (Objects.equals(currentOrder.getId(), order.getId())) {
                return order;
            }
        }
        return null;
    }

    public void synchronizeRequests(List<Request> newRequests) {
        List<Request> requests = getRequests();

        for (Request newRequest : newRequests) {

            Request matchingRequest = findIdMatchingRequest(newRequest, requests);

            if (matchingRequest == null) {
                requests.add(newRequest);
            } else if (!matchingRequest.equals(newRequest)) {
                matchingRequest.setClientId(newRequest.getClientId());
                matchingRequest.setRequestStatus(newRequest.getRequestStatus());
                matchingRequest.setBook(newRequest.getBook());
                matchingRequest.setAmount(newRequest.getAmount());
            }
        }

        if (newRequests.size() != requests.size()) {
            for (Request oldRequest : requests) {
                Request matchingRequest = findIdMatchingRequest(oldRequest, newRequests);

                if (matchingRequest == null) {
                    requests.remove(oldRequest);
                }
            }
        }
    }

    public Request findIdMatchingRequest(Request currentRequest, List<Request> requests) {
        for (Request request : requests) {
            if (Objects.equals(currentRequest.getId(), request.getId())) {
                return request;
            }
        }
        return null;
    }
}
