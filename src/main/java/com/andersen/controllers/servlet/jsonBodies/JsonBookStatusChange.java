package com.andersen.controllers.servlet.jsonBodies;

import com.andersen.models.Book;

public class JsonBookStatusChange {
    private Book.BookStatus status;
    private Long id;

    public JsonBookStatusChange() {
    }

    public Book.BookStatus getStatus() {
        return status;
    }

    public void setStatus(Book.BookStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
