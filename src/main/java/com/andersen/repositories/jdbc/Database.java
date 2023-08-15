package com.andersen.repositories.jdbc;

import com.google.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final DataSource dataSource;

    @Inject
    public Database(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... params) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                if(params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);//TODO fix
                } else {
                    preparedStatement.setString(i + 1, params[i].toString());
                }
            }

            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SqlResult execute(String sql, Object... params) {
        try {
            Connection connection = dataSource.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                preparedStatement.setString(i + 1, params[i].toString());
            }

            return new SqlResult(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static final class SqlResult {

        private final ResultSet resultSet;

        SqlResult(ResultSet resultSet) {
            this.resultSet = resultSet;
        }

        public boolean isPresent() {
            try (resultSet) {
                return resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        <T> List<T> map(RowMapper<T> mapper) {
            try (resultSet) {
                var list = new ArrayList<T>();
                while (resultSet.next()) {
                    list.add(mapper.apply(resultSet));
                }
                return list;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @FunctionalInterface
        interface RowMapper<T> {
            T apply(ResultSet resultSet) throws SQLException;
        }
    }

}
