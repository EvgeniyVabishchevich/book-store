package com.andersen.controllers.servlet;

import com.andersen.annotations.Get;
import com.andersen.annotations.Post;
import com.andersen.annotations.Put;
import com.andersen.controllers.OrderController;
import com.andersen.controllers.servlet.jsonBodies.JsonOrder;
import com.andersen.enums.OrderSortKey;
import com.andersen.models.Order;
import com.andersen.models.Request;
import com.andersen.services.BookService;
import com.andersen.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderControllerServlet implements OrderController {
    private final BookService bookService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public OrderControllerServlet(BookService bookService, OrderService orderService, ObjectMapper objectMapper) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Get("/orders/list")
    public List<Order> getAllSorted(HttpServletRequest request, HttpServletResponse response) {
        String sortKey = request.getParameter("sort");

        OrderSortKey orderSortKey = OrderSortKey.valueOf(sortKey.toUpperCase());

        return orderService.getAllSorted(orderSortKey);
    }

    @Override
    @Post("/orders/add")
    public void addOrder(HttpServletRequest request, HttpServletResponse response) {
        try {
            JsonOrder jsonOrder = objectMapper.readValue(request.getInputStream(), JsonOrder.class);

            Order order = new Order(jsonOrder.getClientId());

            List<Request> requests = jsonOrder.getRequests().stream()
                    .map(jsonRequest -> new Request(order.getId(), jsonOrder.getClientId(), bookService.findById(jsonRequest.getBookId()),
                            jsonRequest.getAmount()))
                    .toList();

            order.changeRequests(requests);

            orderService.add(order);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Put("/orders/complete")
    public void complete(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = objectMapper.readValue(request.getInputStream(), Long.class);

            orderService.complete(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Get("/orders/income")
    public int getIncomeBySpecifiedPeriod(HttpServletRequest request, HttpServletResponse response) {
        LocalDateTime from = LocalDateTime.parse(request.getParameter("from"));
        LocalDateTime to = LocalDateTime.parse(request.getParameter("to"));

        return orderService.getIncomeBySpecifiedPeriod(from, to);
    }

    @Override
    @Put("/orders/cancel")
    public void cancel(HttpServletRequest request, HttpServletResponse response) {
        try {
            Long id = objectMapper.readValue(request.getInputStream(), Long.class);

            orderService.cancel(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
