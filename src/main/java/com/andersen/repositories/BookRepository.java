package com.andersen.repositories;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;

import java.util.List;


public interface BookRepository {
    void save(Book book);
    Book findById(Long bookId);

    List<Book> getAllSorted(BookSortKey sortKey);
}
