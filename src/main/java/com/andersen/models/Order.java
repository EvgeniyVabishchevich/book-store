package com.andersen.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id")
    private Long id;
    @Column(name = "client_id")
    private Long clientId;
    @Column(name = "completion_date")
    private LocalDateTime completionDate;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @JoinColumn(name = "order_id")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Request> requests;
    @Column(name = "order_price")
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
        this.price = countPrice();
    }

    public Order(Long id, Long clientId, LocalDateTime completionDate, OrderStatus status, Integer price) {
        this.id = id;
        this.clientId = clientId;
        this.completionDate = completionDate;
        this.status = status;
        this.price = price;
    }

    public Order(Long clientId) {
        this.id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        this.clientId = clientId;
        this.status = OrderStatus.JUST_CREATED;
    }

    public Order() {

    }

    public void changeRequests(List<Request> requests) {
        this.requests = requests;
        price = countPrice();
    }

    public int countPrice() {
        return requests.stream().
                mapToInt(request -> request.getAmount() * request.getBook().getPrice())
                .sum();
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
        this.requests = new ArrayList<>(requests);
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
