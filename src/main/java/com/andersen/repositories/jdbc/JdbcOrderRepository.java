package com.andersen.repositories.jdbc;

import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.repositories.OrderRepository;
import com.andersen.repositories.jdbc.mapper.OrderMapper;
import com.google.inject.Inject;

import java.util.List;
import java.util.Optional;

public class JdbcOrderRepository implements OrderRepository {

    private final Database database;

    @Inject
    public JdbcOrderRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Order order) {
        if (has(order.getId())) {
            database.update("UPDATE orders SET completion_date = ?, order_status = ?, order_price = ? " +
                            "WHERE order_id = ?",
                    order.getCompletionDate(), order.getStatus(), order.getPrice(), order.getId());
        } else {
            database.update("INSERT INTO orders (order_id, completion_date, order_status, order_price, client_id) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    order.getId(), order.getCompletionDate(), order.getStatus(), order.getPrice(), order.getClientId());
        }
    }

    @Override
    public List<Order> getAllSorted(OrderSortKey sortKey) {

        String sql = "SELECT * FROM orders INNER JOIN requests ON orders.order_id = " +
                "requests.order_id INNER JOIN books ON requests.book_id = books.book_id";

        if (sortKey == OrderSortKey.NATURAL) {
            return database.execute(sql)
                    .map(OrderMapper::apply);
        }

        String orderMethod = "";

        switch (sortKey) {
            case DATE -> orderMethod = "completion_date";
            case PRICE -> orderMethod = "order_price";
            case STATUS -> orderMethod = "order_status";
        }

        return database.execute(sql + " ORDER BY " + orderMethod + " order_id")
                .map(OrderMapper::apply);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.of(database.execute("SELECT * FROM orders INNER JOIN requests ON orders.order_id = " +
                        "requests.order_id INNER JOIN books ON requests.book_id = books.book_id " +
                        "WHERE orders.order_id = ?", id)
                .map(OrderMapper::apply)
                .get(0));//TODO fix
    }

    @Override
    public void remove(Long id) {
        database.update("DELETE FROM orders WHERE order_id = ?", id);
    }

    private boolean has(Long id) {
        return database.execute("SELECT 1 FROM orders WHERE order_id = ?", id).isPresent();
    }
}
