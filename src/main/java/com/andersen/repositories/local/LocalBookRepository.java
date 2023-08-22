package com.andersen.repositories.local;

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
    private final String savePath;

    @Inject
    public LocalBookRepository(ObjectMapper objectMapper, String savePath) {
        this.objectMapper = objectMapper;
        this.savePath = savePath;
    }

    @Override
    public void save(Book book) {
        // TODO make
    }

    @Override
    public Book findById(Long id) {
        return getAllSorted(BookSortKey.NATURAL).stream()
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
        try {
            List<Book> books = objectMapper.readValue(new File(savePath), AppState.class).getBooks();

            switch (sortKey) {
                case NAME -> books.sort(Comparator.comparing(Book::getName));
                case PRICE -> books.sort(Comparator.comparing(Book::getPrice));
                case STATUS -> books.sort(Comparator.comparing(Book::getStatus));
            }

            return books;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void save(List<Book> books) {
        try {
            AppState appState = objectMapper.readValue(new File(savePath), AppState.class);

            appState.synchronizeBooks(books);

            objectMapper.writeValue(new File(savePath), appState);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
