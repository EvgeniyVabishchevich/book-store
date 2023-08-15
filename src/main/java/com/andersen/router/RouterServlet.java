package com.andersen.router;

import com.andersen.annotations.Delete;
import com.andersen.annotations.Get;
import com.andersen.annotations.Post;
import com.andersen.annotations.Put;
import com.andersen.controllers.BookController;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.RequestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Singleton
public class RouterServlet extends HttpServlet {

    private final BookController bookController;
    private final OrderController orderController;
    private final RequestController requestController;

    private final List<RequestHandler> getHandlers;
    private final List<RequestHandler> postHandlers;
    private final List<RequestHandler> putHandlers;
    private final List<RequestHandler> deleteHandlers;
    private final ObjectMapper objectMapper;

    @Inject
    public RouterServlet(BookController bookController, OrderController orderController,
                         RequestController requestController, ObjectMapper objectMapper) {
        this.bookController = bookController;
        this.orderController = orderController;
        this.requestController = requestController;

        this.objectMapper = objectMapper;

        this.getHandlers = findHandlersByHttpMethod(Get.class);
        this.postHandlers = findHandlersByHttpMethod(Post.class);
        this.putHandlers = findHandlersByHttpMethod(Put.class);
        this.deleteHandlers = findHandlersByHttpMethod(Delete.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Object result = getHandlers.stream()
                    .filter(handler -> handler.getMethod().getAnnotation(Get.class).value().equals(req.getRequestURI()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No mapping for GET %s".formatted(req.getRequestURI())))
                    .invoke(req, resp);

            processResult(result, resp);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Object result = postHandlers.stream()
                    .filter(handler -> handler.getMethod().getAnnotation(Post.class).value().equals(req.getRequestURI()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No mapping for POST %s".formatted(req.getRequestURI())))
                    .invoke(req, resp);

            processResult(result, resp);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Object result = deleteHandlers.stream()
                    .filter(handler -> handler.getMethod().getAnnotation(Delete.class).value().equals(req.getRequestURI()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No mapping for DELETE %s".formatted(req.getRequestURI())))
                    .invoke(req, resp);

            processResult(result, resp);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            Object result = putHandlers.stream()
                    .filter(handler -> handler.getMethod().getAnnotation(Put.class).value().equals(req.getRequestURI()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No mapping for PUT %s".formatted(req.getRequestURI())))
                    .invoke(req, resp);

            processResult(result, resp);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processResult(Object result, HttpServletResponse response) {
        if (result == null) {
            return;
        }

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(objectMapper.writeValueAsString(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<RequestHandler> findHandlersByHttpMethod(Class<? extends Annotation> httpMethodAnnotation) {
        return Stream.of(bookController, orderController, requestController)
                .map(controller ->
                        Arrays.stream(controller.getClass().getDeclaredMethods())
                                .filter(method -> Objects.nonNull(method.getAnnotation(httpMethodAnnotation)))
                                .map(method -> new RequestHandler(controller, method))
                                .toList())
                .flatMap(Collection::stream)
                .toList();
    }
}