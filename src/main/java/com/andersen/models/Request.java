package com.andersen.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "requests")
public class Request {
    @Id
    @Column(name = "request_id")
    private Long id;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "order_id")
    private Long orderId;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id", scope = Book.class)
    @JsonIdentityReference(alwaysAsId = true)
    @OneToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "request_status")
    @Enumerated(EnumType.STRING)
    private RequestStatus requestStatus;

    public enum RequestStatus {
        JUST_CREATED, IN_PROCESS, COMPLETED
    }

    public Request(Long orderId, Long clientId, Book book, Integer amount) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.orderId = orderId;
        this.clientId = clientId;
        this.book = book;
        this.amount = amount;
        this.requestStatus = RequestStatus.JUST_CREATED;
    }

    public Request(Long id, Long clientId, Long orderId, Book book, Integer amount, RequestStatus requestStatus) {
        this.id = id;
        this.clientId = clientId;
        this.orderId = orderId;
        this.book = book;
        this.amount = amount;
        this.requestStatus = requestStatus;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return id + ". books = " + book.getName() + ", price = " + book.getPrice() + ", requestAmount = " + amount;
    }
}
