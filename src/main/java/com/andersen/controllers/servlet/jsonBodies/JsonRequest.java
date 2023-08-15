package com.andersen.controllers.servlet.jsonBodies;

public class JsonRequest {
    private Long bookId;
    private int amount;

    public JsonRequest() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
