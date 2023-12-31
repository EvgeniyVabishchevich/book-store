package com.andersen.repositories.jdbc.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@FunctionalInterface
public interface RowMapper<T> {
    T apply(ResultSet resultSet) throws SQLException;
}
