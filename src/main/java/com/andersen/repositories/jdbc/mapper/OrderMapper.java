package com.andersen.repositories.jdbc.mapper;

import com.andersen.models.Book;
import com.andersen.models.Order;
import com.andersen.models.Request;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order apply(ResultSet rs) throws SQLException {
        Timestamp completionStamp = rs.getTimestamp("completion_date");
        LocalDateTime completionDate = completionStamp == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(
                completionStamp.getTime()), ZoneOffset.UTC);

        Order order = new Order(
                rs.getLong("order_id"),
                rs.getLong("client_id"),
                completionDate,
                Order.OrderStatus.valueOf(rs.getString("order_status")),
                rs.getInt("order_price")
        );

        List<Request> requests = new ArrayList<>();

        do {
            if (order.getId() == rs.getLong("order_id")) {
                requests.add(RequestMapper.apply(rs));
            } else {
                break;
            }
        } while (rs.next());

        order.changeRequests(requests);

        rs.previous();

        return order;
    }
}
