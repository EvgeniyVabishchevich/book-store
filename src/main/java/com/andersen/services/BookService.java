package com.andersen.services;

import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;

import java.util.List;

public interface BookService {

    List<Book> getAllSorted(BookSortKey sortKey);

    List<Book> getAll();

    Book findById(Long bookId);

    void changeBookStatus(Long bookId, Book.BookStatus bookStatus);
}
