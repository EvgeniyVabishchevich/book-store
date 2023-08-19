package com.andersen.repositories.jdbc;

import com.andersen.repositories.jdbc.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private final DataSource dataSource;

    public Database(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... params) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);//TODO fix
                } else if (params[i] instanceof Integer) {
                    preparedStatement.setInt(i + 1, (Integer) params[i]);
                } else if (params[i] == null) {
                    preparedStatement.setNull(i + 1, Types.TIMESTAMP);
                } else if (params[i] instanceof LocalDateTime) {
                    LocalDateTime time = (LocalDateTime) params[i];
                    Timestamp timestamp = new Timestamp(time.toInstant(ZoneOffset.UTC).toEpochMilli());
                    preparedStatement.setTimestamp(i + 1, timestamp);
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

            PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Long) {
                    preparedStatement.setLong(i + 1, (Long) params[i]);// TODO fix
                } else {
                    preparedStatement.setString(i + 1, params[i].toString());
                }
            }

            return new SqlResult(preparedStatement.executeQuery(), connection, preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static final class SqlResult {

        private final ResultSet resultSet;
        private final Connection connection;
        private final Statement statement;

        SqlResult(ResultSet resultSet, Connection connection, Statement statement) {
            this.resultSet = resultSet;
            this.connection = connection;
            this.statement = statement;
        }

        public boolean isPresent() {
            try (resultSet; statement; connection) {
                return resultSet.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        <T> List<T> map(RowMapper<T> mapper) {
            try (resultSet; statement; connection) {
                List<T> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(mapper.apply(resultSet));
                }
                return list;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
