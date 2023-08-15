package com.andersen.controllers.servlet;

import com.andersen.annotations.Get;
import com.andersen.annotations.Put;
import com.andersen.controllers.BookController;
import com.andersen.controllers.servlet.jsonBodies.JsonBookStatusChange;
import com.andersen.enums.BookSortKey;
import com.andersen.models.Book;
import com.andersen.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Singleton
public class BookControllerServlet implements BookController {

    private final BookService bookService;
    private final ObjectMapper objectMapper;

    @Inject
    public BookControllerServlet(BookService bookService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Get("/books/list")
    public List<Book> getAll(HttpServletRequest request, HttpServletResponse response) {
        return bookService.getAll();
    }

    @Override
    @Get("/books/sorted")
    public List<Book> getAllSorted(HttpServletRequest request, HttpServletResponse response) {
        BookSortKey bookSortKey = BookSortKey.valueOf(request.getParameter("sort").toUpperCase());

        return bookService.getAllSorted(bookSortKey);
    }

    @Override
    @Put("/books/change-status")
    public void changeBookStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            JsonBookStatusChange statusChange = objectMapper.readValue(request.getInputStream(),
                    JsonBookStatusChange.class);

            bookService.changeBookStatus(statusChange.getId(), statusChange.getStatus());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


