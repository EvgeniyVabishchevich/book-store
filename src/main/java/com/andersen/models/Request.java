package com.andersen.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;

public class Request {

    private Long id;
    private Long clientId;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id", scope = Book.class)
    @JsonIdentityReference(alwaysAsId = true)
    private Book book;

    private Integer amount;

    private RequestStatus requestStatus;

    public enum RequestStatus {
        JUST_CREATED, IN_PROCESS, COMPLETED
    }

    public Request(Long clientId, Book book, Integer amount) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.clientId = clientId;
        this.book = book;
        this.amount = amount;
        this.requestStatus = RequestStatus.JUST_CREATED;
    }

    public Request() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    @Override
    public String toString() {
        return id + ". books = " + book.getName() + ", price = " + book.getPrice() + ", requestAmount = " + amount;
    }
}
