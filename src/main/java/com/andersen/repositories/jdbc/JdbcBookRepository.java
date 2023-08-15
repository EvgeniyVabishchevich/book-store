package com.andersen.repositories.jdbc;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.repositories.BookRepository;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcBookRepository implements BookRepository {
    private final Database database;

    @Inject
    public JdbcBookRepository(Database database) {
        this.database = database;
    }

    @Override
    public Book findById(Long bookId) {
        return database.execute("SELECT * FROM books WHERE id = ?", bookId)
                .map(this::generateBookFromResultSet).get(0);
    }

    @Override
    public List<Book> getAllSorted(BookSortKey sortKey) {
        if (sortKey == BookSortKey.NATURAL) {
            return database.execute("SELECT * FROM books")
                    .map(this::generateBookFromResultSet);
        }

        return database.execute("SELECT * FROM books ORDER BY " + sortKey.name().toLowerCase())
                .map(this::generateBookFromResultSet);
    }

    @Override
    public void changeBookStatus(Long id, Book.BookStatus status) {
        database.update("UPDATE books SET status = ? WHERE id = ?", status, id);
    }

    private Book generateBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                Book.BookStatus.valueOf(resultSet.getString("status")),
                resultSet.getInt("price")
        );
    }
}
