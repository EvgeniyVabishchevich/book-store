package com.andersen.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id", scope = Book.class)
public class Book {

    private Long id;
    private String name;
    private BookStatus status;
    private Integer price;

    public enum BookStatus {
        IN_STOCK, OUT_OF_STOCK
    }

    public Book() {
    }

    public Book(Long id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = BookStatus.IN_STOCK;
    }

    public Book(Long id, String name, BookStatus status, Integer price) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + ". name = " + name + ", price = " + price + ", status = " + status;
    }


}
