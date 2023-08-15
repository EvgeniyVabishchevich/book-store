package com.andersen.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {

    private Long id;
    private Long clientId;
    private LocalDateTime completionDate;
    private OrderStatus status;
    private List<Request> requests;
    private Integer price;

    public enum OrderStatus {
        JUST_CREATED, IN_PROCESS, COMPLETED, CANCELED
    }

    public Order(Long id, Long clientId, LocalDateTime completionDate, OrderStatus status, List<Request> requests,
                 Integer price) {
        this.id = id;
        this.clientId = clientId;
        this.completionDate = completionDate;
        this.status = status;
        this.requests = requests;
        this.price = price;
    }

    public Order(Long clientId, List<Request> requests) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.clientId = clientId;
        this.status = OrderStatus.JUST_CREATED;
        this.requests = requests;
        this.price = requests.stream().
                mapToInt(request -> request.getAmount() * request.getBook().getPrice())
                .sum();
    }

    public Order() {

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

    public LocalDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<Request> getRequests() {
        if (requests == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(requests);
    }

    public void setRequests(List<Request> requests) {
        if (requests == null) {
            this.requests = new ArrayList<>();
        } else {
            this.requests = new ArrayList<>(requests);
        }
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return id
                + ". clientId = " + clientId
                + (completionDate == null ? ", " : ", completionDate = " + completionDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + ", ")
                + "price = " + price + ", "
                + "status = " + status;

    }
}
