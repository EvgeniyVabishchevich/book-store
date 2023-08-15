package com.andersen.repositories.local;

import com.andersen.config.model.ConfigModel;
import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.repositories.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Singleton
public class LocalBookRepository implements BookRepository {
    private final ObjectMapper objectMapper;
    private final ConfigModel configModel;

    @Inject
    public LocalBookRepository(ObjectMapper objectMapper, ConfigModel configModel) {
        this.objectMapper = objectMapper;
        this.configModel = configModel;
    }

    @Override
    public List<Book> getAll() {
        try {
            return objectMapper.readValue(new File(configModel.savePath()), AppState.class).getBooks();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book findById(Long id) {
        return getAll().stream()
                .filter(book -> Objects.equals(book.getId(), id))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Wrong book id"));
    }

    public Book findById(List<Book> books, Long id) {
        for (Book book : books) {
            if (Objects.equals(book.getId(), id)) {
                return book;
            }
        }
        throw new IllegalArgumentException("Wrong books id");
    }

    @Override
    public List<Book> getAllSorted(BookSortKey sortKey) {
        List<Book> books = getAll();

        sort(books, sortKey);
        return books;
    }

    @Override
    public void changeBookStatus(Long id, Book.BookStatus status) {
        List<Book> books = getAll();

        Book searchedBook = findById(books, id);

        searchedBook.setStatus(status);

        save(books);
    }

    private void sort(List<Book> books, BookSortKey bookSortKey) {
        switch (bookSortKey) {
            case NAME -> books.sort(Comparator.comparing(Book::getName));
            case PRICE -> books.sort(Comparator.comparing(Book::getPrice));
            case STATUS -> books.sort(Comparator.comparing(Book::getStatus));
            default -> throw new IllegalArgumentException("Unexpected value: " + bookSortKey);
        }
    }

    private void save(List<Book> books) {
        try {
            AppState appState = objectMapper.readValue(new File(configModel.savePath()), AppState.class);

            appState.synchronizeBooks(books);

            objectMapper.writeValue(new File(configModel.savePath()), appState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
