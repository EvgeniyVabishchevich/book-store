package com.andersen.repositories.jdbc;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.repositories.BookRepository;
import com.andersen.repositories.jdbc.mapper.BookMapper;

import java.util.List;

public class JdbcBookRepository implements BookRepository {
    private final Database database;

    public JdbcBookRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Book book) {
        database.update("UPDATE books SET book_status = ? WHERE book_id = ?", book.getStatus(), book.getId());
    }

    @Override
    public Book findById(Long bookId) {
        return database.execute("SELECT * FROM books WHERE book_id = ?", bookId)
                .map(BookMapper::apply).get(0);
    }

    @Override
    public List<Book> getAllSorted(BookSortKey sortKey) {
        if (sortKey == BookSortKey.NATURAL) {
            return database.execute("SELECT * FROM books")
                    .map(BookMapper::apply);
        }

        return database.execute("SELECT * FROM books ORDER BY book_" + sortKey.name().toLowerCase())
                .map(BookMapper::apply);
    }
}
