package com.andersen.repositories.jdbc;

import com.andersen.enums.RequestSortKey;
import com.andersen.models.Request;
import com.andersen.repositories.RequestRepository;
import com.andersen.repositories.jdbc.mapper.RequestMapper;

import java.util.List;

public class JdbcRequestRepository implements RequestRepository {

    private final Database database;

    public JdbcRequestRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Request request) {
        if (has(request.getId())) {
            database.update("UPDATE requests SET amount = ?, request_status = ? WHERE request_id = ?",
                    request.getAmount(), request.getRequestStatus(), request.getId());
        } else {
            database.update("INSERT INTO requests (request_id, book_id, order_id, amount, request_status) " +
                            "VALUES (?, ?, ?, ?, ?)", request.getId(), request.getBook().getId(), request.getOrderId(),
                    request.getAmount(), request.getRequestStatus());
        }
    }

    @Override
    public List<Request> getAllSorted(RequestSortKey sortKey) {
        String sql = "SELECT * FROM requests INNER JOIN books ON requests.book_id = books.book_id";

        if (sortKey == RequestSortKey.NATURAL) {
            return database.execute(sql)
                    .map(RequestMapper::apply);
        }

        String orderMethod = "";

        switch (sortKey) {
            case PRICE -> orderMethod = "requests.amount * books.book_price";
            case NAME -> orderMethod = "request_name";
        }

        return database.execute(sql + " ORDER BY " + orderMethod)
                .map(RequestMapper::apply);
    }

    @Override
    public List<Request> findAllByOrderId(Long orderId) {
        return database.execute("SELECT * FROM requests INNER JOIN books ON requests.book_id = books.book_id" +
                        " WHERE order_id = ?", orderId)
                .map(RequestMapper::apply);
    }

    @Override
    public List<Request> findAllByBookId(Long bookId) {
        return database.execute("SELECT * FROM requests INNER JOIN books ON requests.book_id = books.book_id" +
                        " WHERE requests.book_id = ?", bookId)
                .map(RequestMapper::apply);
    }

    private boolean has(Long id) {
        return database.execute("SELECT 1 FROM requests WHERE request_id = ?", id).isPresent();
    }
}
