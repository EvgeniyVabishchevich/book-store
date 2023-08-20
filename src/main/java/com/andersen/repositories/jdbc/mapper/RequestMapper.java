package com.andersen.repositories.jdbc.mapper;

import com.andersen.models.Request;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestMapper {
    public static Request apply(ResultSet rs) throws SQLException {
        return new Request(
                rs.getLong("request_id"),
                rs.getLong("client_id"),
                rs.getLong("order_id"),
                BookMapper.apply(rs),
                rs.getInt("amount"),
                Request.RequestStatus.valueOf(rs.getString("request_status"))
        );
    }
}
