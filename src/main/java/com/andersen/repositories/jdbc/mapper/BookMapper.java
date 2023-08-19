package com.andersen.repositories.jdbc.mapper;

import com.andersen.models.Book;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookMapper {

    public static Book apply(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("book_id"),
                rs.getString("book_name"),
                Book.BookStatus.valueOf(rs.getString("book_status")),
                rs.getInt("book_price")
        );
    }
}
